package com.hkcapital.portoflio.service.api.etoro.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveResponseMapper;
import com.hkcapital.portoflio.service.marketfeed.subscriber.impl.BuySellSignalGeneratorSub;
import com.hkcapital.portoflio.service.marketfeed.subscriber.impl.MarketFeedDbWriterSub;
import com.hkcapital.portoflio.service.instrument.InstrumentService;
import com.hkcapital.portoflio.service.marketfeed.observer.MarketFeedObserver;
import com.hkcapital.portoflio.service.orders.OrderManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

@Service
public class EtoroWebServiceSocketManagerImpl implements com.hkcapital.portoflio.service.api.etoro.EtoroWebSocketManagerService
{
    private final Logger logger = LoggerFactory.getLogger(EtoroWebServiceSocketManagerImpl.class);

    private final com.hkcapital.portoflio.service.orders.OrderManagerService orderManagerService;
    private final com.hkcapital.portoflio.service.instrument.InstrumentService instrumentService;

    private final EtoroApiConfiguration etoroApiConfiguration;
    private final ObjectMapper objectMapper;

    private final MarketFeedObserver marketFeedObserver;
    private final LiveResponseMapper liveResponseMapper;
    private final MarketFeedDbWriterSub marketFeedDbWriter;
    private final BuySellSignalGeneratorSub buySellManager;

    public EtoroWebServiceSocketManagerImpl(final com.hkcapital.portoflio.service.srmatrix.SRMatrixService srMatrixService, //
                                            final OrderManagerService orderManagerService, //
                                            final InstrumentService instrumentService, //
                                            final com.hkcapital.portoflio.service.strategy.StrategyService strategyService, //
                                            final com.hkcapital.portoflio.service.positions.PositionService positionService, //
                                            final EtoroApiConfiguration etoroApiConfiguration, //
                                            final ObjectMapper objectMapper, //
                                            final MarketFeedObserver marketFeedObserver, //
                                            final LiveResponseMapper liveResponseMapper,
                                            final MarketFeedDbWriterSub marketFeedDbWriter,
                                            final BuySellSignalGeneratorSub buySellManager)
    {

        this.orderManagerService = orderManagerService;
        this.instrumentService = instrumentService;
        this.etoroApiConfiguration = etoroApiConfiguration;
        this.objectMapper = objectMapper;
        this.marketFeedObserver = marketFeedObserver;
        this.liveResponseMapper = liveResponseMapper;
        this.marketFeedDbWriter = marketFeedDbWriter;
        this.buySellManager = buySellManager;
        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);
        marketFeedObserver.addMarketFeedSubscriber(buySellManager);
    }

    @Override
    public void subscribeAndSchedule()
    {

        StartWebSocketRunner startWebSocket = //
                new StartWebSocketRunner(etoroApiConfiguration, marketFeedObserver, //
                        liveResponseMapper, instrumentService, objectMapper);
        new Thread(startWebSocket).start();

        ScheduledExecutorService scheduler = newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() ->
        {
            try
            {
                orderManagerService.fetchAndCloseEtoroOrder();
//
//                logger.info("Executing background orders...");
//
//                LiveInstrumentRate instrumentRate = eToroWSClient.getLiveInstrumentRate();
//
//                Instrument instrument = instrumentService.findAll().stream().filter(e -> e.getEtoroInstrumentId() != null && e.getEtoroInstrumentId().intValue() //
//                                == Instruments.GOLD.getInstrumentId()//
//                                .intValue() && e.getActive()).findAny()//
//                        .get();
//
//                Double maxSlippage = instrument.getMaxSlippage();
//
//                if (instrumentRate != null && instrumentRate.getAsk() != null && instrumentRate.getBid() != null && instrument.getEtoroInstrumentId().intValue() == instrumentRate.getInstrumentId().intValue())
//                {
//                    if (Configuration.ACTIVATE_AUTOMATIC_TRADING)
//                    {
//                        logger.info("Sending Automatic trade to etoro!");
//                        Double ask = instrumentRate.getAsk();
//                        Double bid = instrumentRate.getBid();
//                        Double slippage = ask - bid;
//                        if (slippage > maxSlippage)
//                        {
//                            logger.info("Unusual price received, Order rejected due to high slippage,  bid = [{}] , ask = [{}], slippage = [{}] , maxSlippage = [{}]", bid, ask, slippage, maxSlippage);
//                            return;
//                        }
//                        logger.info("Instrument price received bid = [{}] , ask = [{}] slippage = [{}] , maxSlippage = [{}] sending order for execution", bid, ask, slippage, maxSlippage);
//
//                        List<Strategy> strategies = //
//                                strategyService.findAll()//
//                                        .stream()//
//                                        .filter(Strategy::getActive)//
//                                        .collect(Collectors.toList());
//
//                        for (Strategy strategy : strategies)
//                        {
//                            List<Position> positions = positionService.findByStrategyId(strategy.getId());
//
//                            for (Position position : positions)
//                            {
//                                Instrument inst = position.getInstrument();
//
//                                Integer leverage = position.getConfiguration().getLev();
//
//                                Optional<SRMatrix> srMatrix = this.srMatrixService.findAll().stream().filter(matrix -> matrix.getActive() && "MINUTE".equals(matrix.getTimeFrameUnit()) && matrix.getTimeFrame() == 5).findAny();
//
//                                if (!srMatrix.isPresent())
//                                {
//                                    logger.info("No SR-Matrix found, please check SR-Matrix!");
//                                    return;
//                                }
//
//                                if (ask <= srMatrix.get().getSupport()) //
//                                {
//                                    logger.info("Sending buy order instrument = {} ", inst.getEtoroInstrumentId());
//
//                                    EtoroMarketOrderDto etoroMarketBuyOrder = EtoroMarketOrderDto.builder().instrumentId(inst.getEtoroInstrumentId()).isBuy(true).leverage(leverage).amount(position.getCurrentPositionEquity()).stopLossRate(null).takeProfitRate(srMatrix.get().getResistance()).isTslEnabled(null).isNoTakeProfit(null).isNoStopLoss(null).orderType(OrderTypes.AUTO.getOrderType()).bid(bid).ask(ask).maxAllowedSlippage(maxSlippage).etoroSlippage(slippage).build();
//                                    orderManagerService.createAndSaveMarketOrder((etoroMarketBuyOrder));
//                                } else if (ask >= srMatrix.get().getResistance())
//                                {
//                                    logger.info("Sending sell order order instrument = {} ", inst.getEtoroInstrumentId());
//
//                                    EtoroMarketOrderDto etoroMarketSellOrder = EtoroMarketOrderDto.builder().instrumentId(inst.getEtoroInstrumentId()).isBuy(false).leverage(leverage).amount(position.getCurrentPositionEquity()).stopLossRate(null).takeProfitRate(srMatrix.get().getSupport()).isTslEnabled(null).isNoTakeProfit(null).isNoStopLoss(null).orderType(OrderTypes.AUTO.getOrderType()).bid(bid).ask(ask).maxAllowedSlippage(maxSlippage).etoroSlippage(slippage).build();
//                                    orderManagerService.createAndSaveMarketOrder((etoroMarketSellOrder));
//                                } else
//                                {
//                                    logger.info("Automatic trading is not yet activated!");
//                                }
//                            }
//                        }
//                    }
//
//                }
            } catch (Exception e)
            {
                logger.error("Error in background task", e);
            }
        }, 5, 5, TimeUnit.MINUTES);
    }
}
