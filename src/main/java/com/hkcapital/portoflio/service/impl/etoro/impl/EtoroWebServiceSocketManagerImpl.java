package com.hkcapital.portoflio.service.impl.etoro.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.etoro.Configuration;
import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.master.Instruments;
import com.hkcapital.portoflio.etoro.websocket.EToroWSClient;
import com.hkcapital.portoflio.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.model.SRMatrix;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.order.OrderTypes;
import com.hkcapital.portoflio.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.concurrent.Executors.*;

@Service
public class EtoroWebServiceSocketManagerImpl implements EtoroWebSocketManagerService
{
    private final Logger logger = LoggerFactory.getLogger(EtoroWebServiceSocketManagerImpl.class);
    private final SRMatrixService srMatrixService;
    private final OrderManagerService orderManagerService;
    private final InstrumentService instrumentService;
    private final StrategyService strategyService;
    private final PositionService positionService;
    private final EtoroApiConfiguration etoroApiConfiguration;
    private final ObjectMapper objectMapper;

    public EtoroWebServiceSocketManagerImpl(final SRMatrixService srMatrixService,
                                            final OrderManagerService orderManagerService,
                                            final InstrumentService instrumentService,
                                            final StrategyService strategyService,
                                            final PositionService positionService,
                                            final EtoroApiConfiguration etoroApiConfiguration,
                                            final ObjectMapper objectMapper)
    {
        this.srMatrixService = srMatrixService;
        this.orderManagerService = orderManagerService;
        this.instrumentService = instrumentService;
        this.strategyService = strategyService;
        this.positionService = positionService;
        this.etoroApiConfiguration = etoroApiConfiguration;
        this.objectMapper = objectMapper;
    }

    @Override
    public void subscribeAndSchedule()
    {
        EToroWSClient eToroWSClient = new EToroWSClient(instrumentService, objectMapper);
        StartWebSocketRunner startWebSocket = new StartWebSocketRunner(eToroWSClient, etoroApiConfiguration);
        new Thread(startWebSocket).start();
        ScheduledExecutorService scheduler = newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() ->
        {
            try
            {
                orderManagerService.fetchAndCloseEtoroOrder();

                logger.info("Executing background orders...");

                LiveInstrumentRate instrumentRate = eToroWSClient.getLiveInstrumentRate();

                Instrument instrument = instrumentService.findAll().stream()
                        .filter(e -> e.getEtoroInstrumentId() != null && e.getEtoroInstrumentId().intValue() //
                                == Instruments.GOLD.getInstrumentId()//
                                .intValue() && e.getActive()).findAny()//
                        .get();

                Double maxSlippage = instrument.getMaxSlippage();

                if (instrumentRate != null && instrumentRate.getAsk() != null && instrumentRate.getBid() != null &&
                 instrument.getEtoroInstrumentId().intValue() == instrumentRate.getInstrumentId().intValue())
                {
                    if (Configuration.ACTIVATE_AUTOMATIC_TRADING)
                    {
                        logger.info("Sending Automatic trade to etoro!");
                        Double ask = instrumentRate.getAsk();
                        Double bid = instrumentRate.getBid();
                        Double slippage = ask - bid;
                        if (slippage > maxSlippage)
                        {
                            logger.info("Unusual price received, Order rejected due to high slippage,  bid = [{}] , ask = [{}], slippage = [{}] , maxSlippage = [{}]"
                                    , bid, ask, slippage, maxSlippage);
                            return;
                        }
                        logger.info("Instrument price received bid = [{}] , ask = [{}] slippage = [{}] , maxSlippage = [{}] sending order for execution",
                                bid, ask, slippage, maxSlippage);

                        List<Strategy> strategies = //
                                strategyService.findAll()//
                                        .stream()//
                                        .filter(Strategy::getActive)//
                                        .collect(Collectors.toList());

                        for (Strategy strategy : strategies)
                        {
                            List<Position> positions = positionService.findByStrategyId(strategy.getId());

                            for (Position position : positions)
                            {
                                Instrument inst = position.getInstrument();

                                Integer leverage = position.getConfiguration().getLev();

                                Optional<SRMatrix> srMatrix = this.srMatrixService.findAll()
                                        .stream()
                                        .filter(matrix -> matrix.getActive() && "MINUTE".equals(matrix.getTimeFrameUnit()) &&
                                                matrix.getTimeFrame() == 5).findAny();

                                if (!srMatrix.isPresent())
                                {
                                    logger.info("No SR-Matrix found, please check SR-Matrix!");
                                    return;
                                }

                                if (ask <= srMatrix.get().getSupport()) //
                                {
                                    logger.info("Sending buy order instrument = {} ", inst.getEtoroInstrumentId());

                                    EtoroMarketOrderDto etoroMarketBuyOrder =
                                            EtoroMarketOrderDto.builder()
                                                    .instrumentId(inst.getEtoroInstrumentId())
                                                    .isBuy(true)
                                                    .leverage(leverage)
                                                    .amount(position.getCurrentPositionEquity())
                                                    .stopLossRate(null)
                                                    .takeProfitRate(srMatrix.get().getResistance())
                                                    .isTslEnabled(null)
                                                    .isNoTakeProfit(null)
                                                    .isNoStopLoss(null)
                                                    .orderType(OrderTypes.AUTO.getOrderType())
                                                    .bid(bid)
                                                    .ask(ask)
                                                    .maxAllowedSlippage(maxSlippage)
                                                    .etoroSlippage(slippage)
                                                    .build();
                                    orderManagerService.createAndSaveMarketOrder((etoroMarketBuyOrder));
                                } else if (ask >= srMatrix.get().getResistance())
                                {
                                    logger.info("Sending sell order order instrument = {} ", inst.getEtoroInstrumentId());

                                    EtoroMarketOrderDto etoroMarketSellOrder =
                                            EtoroMarketOrderDto.builder()
                                                    .instrumentId(inst.getEtoroInstrumentId())
                                                    .isBuy(false)
                                                    .leverage(leverage)
                                                    .amount(position.getCurrentPositionEquity())
                                                    .stopLossRate(null)
                                                    .takeProfitRate(srMatrix.get().getSupport())
                                                    .isTslEnabled(null)
                                                    .isNoTakeProfit(null)
                                                    .isNoStopLoss(null)
                                                    .orderType(OrderTypes.AUTO.getOrderType())
                                                    .bid(bid)
                                                    .ask(ask)
                                                    .maxAllowedSlippage(maxSlippage)
                                                    .etoroSlippage(slippage)
                                                    .build();
                                    orderManagerService.createAndSaveMarketOrder((etoroMarketSellOrder));
                                } else
                                {
                                    logger.info("Automatic trading is not yet activated!");
                                }
                            }
                        }
                    }

                }
            } catch (Exception e)
            {
                logger.error("Error in background task", e);
            }
        }, 5, 5, TimeUnit.MINUTES);
    }
}
