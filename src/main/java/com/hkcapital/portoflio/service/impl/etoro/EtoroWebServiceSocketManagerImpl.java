package com.hkcapital.portoflio.service.impl.etoro;

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


    public EtoroWebServiceSocketManagerImpl(final SRMatrixService srMatrixService,
                                            final OrderManagerService orderManagerService,
                                            final InstrumentService instrumentService,
                                            final StrategyService strategyService,
                                            final PositionService positionService,
                                            final EtoroApiConfiguration etoroApiConfiguration)
    {
        this.srMatrixService = srMatrixService;
        this.orderManagerService = orderManagerService;
        this.instrumentService = instrumentService;
        this.strategyService = strategyService;
        this.positionService = positionService;
        this.etoroApiConfiguration = etoroApiConfiguration;
    }

    @Override
    public void subscribeAndSchedule()
    {
        EToroWSClient eToroWSClient = new EToroWSClient(instrumentService);
        StartWebSocket startWebSocket = new StartWebSocket(eToroWSClient, etoroApiConfiguration);
        new Thread(startWebSocket).start();
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() ->
        {
            try
            {
                logger.info("Executing background orders...");

                LiveInstrumentRate instrumentRate = eToroWSClient.getLiveInstrumentRate();

                Instrument instrument = instrumentService.findAll().stream()
                        .filter(e -> e.getEtoroInstrumentId() != null && e.getEtoroInstrumentId().intValue() //
                                == Instruments.GOLD.getInstrumentId()//
                                .intValue()).findAny()//
                        .get();

                Double maxSlippage = instrument.getMaxSlippage();

                if (instrumentRate != null && instrumentRate.getAsk() != null && instrumentRate.getBid() != null)
                {
                    if (Configuration.ACTIVATE_AUTOMATIC_TRADING)
                    {
                        logger.info("Sending Automatic trade to etoro!");
                        Double ask = Double.parseDouble(instrumentRate.getAsk());
                        Double bid = Double.parseDouble(instrumentRate.getBid());
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
//                        Double tp = (calculateTargetPrice(Double.parseDouble(instrumentRate.getAsk()), position.getConfigurtaion().getLev(),
//                                position.getCurrentPositionEquity(), 0.50));
                        }
                    }

                }
            } catch (Exception e)
            {
                logger.error("Error in background task", e);
            }
        }, 0, 1, TimeUnit.MINUTES);
    }
}

class StartWebSocket implements Runnable
{

    private EToroWSClient eToroWSClient;

    private final EtoroApiConfiguration etoroApiConfiguration;

    StartWebSocket(EToroWSClient eToroWSClient,
                   EtoroApiConfiguration etoroApiConfiguration)
    {
        this.eToroWSClient = eToroWSClient;
        this.etoroApiConfiguration = etoroApiConfiguration;
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run()
    {
        try
        {
            eToroWSClient.start(this.etoroApiConfiguration);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public EToroWSClient geteToroWSClient()
    {
        return eToroWSClient;
    }
}