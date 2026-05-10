package com.hkcapital.portflio.service.orders.impl.etoro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portflio.broker.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portflio.broker.etoro.dto.order.EtoroOrderDetailsResponseDTO;
import com.hkcapital.portflio.broker.etoro.dto.portfolio.EtoroPortfolioPositionDTO;
import com.hkcapital.portflio.broker.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portflio.indicators.CandleBuilder;
import com.hkcapital.portflio.indicators.Unit;
import com.hkcapital.portflio.market.structure.MarketAction;
import com.hkcapital.portflio.market.structure.MarketRegime;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.Position;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.model.etoro.EtoroOrder;
import com.hkcapital.portflio.repository.orders.etoro.EtoroOrderRepository;
import com.hkcapital.portflio.service.api.etoro.EtoroApiService;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.orders.OrderManagerService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import com.hkcapital.portflio.values.order.OrderStatus;
import com.hkcapital.portflio.values.order.OrderTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for sending , closing orders directly in etoro and in local db
 *
 * @author haroon
 * @since 04.2026
 */
@Service
public class EtoroOrderManagerServiceImpl implements OrderManagerService
{
    private static final Logger logger = LoggerFactory.getLogger(EtoroOrderManagerServiceImpl.class);
    private final EtoroOrderRepository orderRepository;
    private final EtoroApiService etoroApiService;
    private final InstrumentService instrumentService;
    private final StrategyService strategyService;
    private final PositionService positionService;

    private final MarketStructure marketStructure;

    public EtoroOrderManagerServiceImpl(final EtoroOrderRepository orderRepository, //
                                        final EtoroApiService etoroApiService,
                                        final InstrumentService instrumentService,
                                        final StrategyService strategyService,
                                        final PositionService positionService,
                                        final MarketStructure marketStructure
    )
    {
        this.orderRepository = orderRepository;
        this.etoroApiService = etoroApiService;
        this.instrumentService = instrumentService;
        this.strategyService = strategyService;
        this.positionService = positionService;
        this.marketStructure = marketStructure;
    }

    /**
     * Create a makert order directly in etoro and in local database
     *
     * @param etoroMarketOrderDto {@link  EtoroMarketOrderDto}
     * @return {@link  EtoroOrder}
     */
    @Override
    public EtoroOrder createAndSaveMarketOrder(final EtoroMarketOrderDto etoroMarketOrderDto)
    {
        logger.info("Send and saving etoro order for instrument [{}]", etoroMarketOrderDto.getInstrumentId());
        try
        {
            final List<EtoroOrder> orders = //
                    orderRepository.findByInstrumentIDAndOderTypeAndStatus(etoroMarketOrderDto.getInstrumentId(), //
                            OrderTypes.AUTO.getOrderType(), OrderStatus.SENT.getOrderStatus()); //

            if (orders.size() > 0)
            {
                logger.error("open order already exist for instrument  {}", etoroMarketOrderDto.getInstrumentId());
                return null;
            }

            final EtoroOrderDetailsResponseDTO orderResponse = etoroApiService.createMarketOrder(etoroMarketOrderDto);

            if (orderResponse != null) //
            {

                final EtoroOrderDetails orderDetails = orderResponse.getOrderForOpen();

                return saveOrder(etoroMarketOrderDto, orderDetails, orderResponse.getToken());
            }

            return null;


        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EtoroOrder saveOrder(final EtoroMarketOrderDto etoroMarketOrderDto,
                                final EtoroOrderDetails orderDetails,
                                final String etoroOrderToken)
    {
        final EtoroOrder etoroOrder = new EtoroOrder();
        etoroOrder.setStatus(OrderStatus.SENT.getOrderStatus());
        etoroOrder.setOderType(etoroMarketOrderDto.getOrderType());
        etoroOrder.fill(orderDetails);
        etoroOrder.setTokenId(etoroOrderToken);
        etoroOrder.setBid(etoroMarketOrderDto.getBid());
        etoroOrder.setAsk(etoroMarketOrderDto.getAsk());
        etoroOrder.setMaxAllowedSlippage(etoroMarketOrderDto.getMaxAllowedSlippage());
        etoroOrder.setEtoroSlippage(etoroMarketOrderDto.getEtoroSlippage());
        etoroOrder.setOrderInfo(etoroMarketOrderDto.getOrderInfo());
        orderRepository.save(etoroOrder);
        return etoroOrder;
    }

    @Override
    public List<EtoroOrder> findByInstrumentIDAndOderType(Integer InstrumentID, String oderType)
    {
        return orderRepository.findByInstrumentIDAndOderType(InstrumentID, oderType);
    }

    @Override
    public EtoroOrder closeEtoroOrder(final Integer etoroOrderId)
    {
        final EtoroOrder order = orderRepository.findById(etoroOrderId).get();
        order.setStatus(OrderStatus.CLOSED.getOrderStatus());
        return orderRepository.save(order);
    }

    @Override
    public List<EtoroOrder> findByInstrumentID(final Integer InstrumentID)
    {
        return orderRepository.findByInstrumentID(InstrumentID);
    }

    @Override
    public List<EtoroOrder> fetchAndCloseEtoroOrder()
    {
        final List<EtoroOrder> closedOrder = new ArrayList<>();

        final EtoroPortfolioResponseDTO etoroPortfolioResponseDTO = etoroApiService.etoroPortfolio();

        final List<Long> openPositions = etoroPortfolioResponseDTO.getClientPortfolio() //
                .getPositions() //
                .stream() //
                .mapToLong(EtoroPortfolioPositionDTO::getOrderId) //
                .boxed() //
                .toList();

        orderRepository.findAll().forEach(order ->  //
        {
            long existedOrder = openPositions.stream().filter(o -> o == order.getOrderID()).count();

            if (existedOrder == 0)
            {
                closedOrder.add(closeEtoroOrder(orderRepository.findById(order.getId()).get().getId()));
            }
        });
        return closedOrder;
    }

    @Override
    public void process(LiveInstrumentRate instrumentRate, SignalBuilder signalBuilder)
    {

        marketStructure.process(instrumentRate, signalBuilder);

        final MarketAction action = marketStructure.getAction();

        final MarketRegime marketRegime = marketStructure.getMarketRegime();

        Instrument instrument = instrumentService.findAll().stream().filter(Instrument::getActive).findAny()//
                .get();

        Double maxSlippage = instrument.getMaxSlippage();

        if (instrumentRate != null && instrumentRate.getAsk() != null && instrumentRate.getBid() != null
                && instrument.getEtoroInstrumentId().intValue() == instrumentRate.getInstrumentId().intValue())
        {
            if (TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING)
            {
                logger.info("Sending Automatic trade to etoro!");
                Double ask = instrumentRate.getAsk();
                Double bid = instrumentRate.getBid();
                Double slippage = ask - bid;
                logger.info("Instrument price received bid = [{}] , ask = [{}] slippage = [{}] , maxSlippage = [{}] sending order for execution", bid, ask, slippage, maxSlippage);
                logger.info("No of candles generated {} ", signalBuilder.getCandleBuilder1Min().candles().size());
                List<Strategy> strategies = //
                        strategyService.findAll()//
                                .stream()//
                                .filter(Strategy::getActive)//
                                .toList();

                for (Strategy strategy : strategies)
                {
                    final List<Position> positions = positionService.findByStrategyId(strategy.getId());

                    for (Position position : positions)
                    {
                        final SRMatrix srMatrix = position.getSrMatrix();
                        final Instrument inst = position.getInstrument();
                        final Integer leverage = position.getConfiguration().getLev();
                        final Integer srTimeFrame = srMatrix.getTimeFrame();
                        final String srTimeFrameUnit = srMatrix.getTimeFrameUnit();
                        final double support = srMatrix.getSupport();
                        final double resistance = srMatrix.getResistance();

                        if (srTimeFrameUnit.equals(Unit.HOUR.getUnit()) && srTimeFrame == 1)
                        {

                            processOneHourTimeFrame(instrumentRate, maxSlippage, ask, bid, position, inst, leverage, support, resistance);
                            return;
                        }

                        if (srTimeFrameUnit.equals(Unit.HOUR.getUnit()) && srTimeFrame == 4)
                        {
                            processFourHourTimeFrame(instrumentRate, position, inst);
                            return;
                        }

                        if (inst.getActive())
                        {
                            processOneMinuteTimeFrame(instrumentRate, signalBuilder, position, instrument);
                        }
                    }
                }
            }

        }
    }

    private void processOneMinuteTimeFrame(LiveInstrumentRate instrumentRate,
                                           SignalBuilder signalBuilder,
                                           Position position,
                                           Instrument inst)
    {

        String srTimeFrameUnit = position.getSrMatrix().getTimeFrameUnit();
        final Double ask = instrumentRate.getAsk();
        final Double bid = instrumentRate.getBid();
        final Double maxSlippage = inst.getMaxSlippage();
        final Double slippage = ask - bid;
        final Integer leverage = position.getConfiguration().getLev();
        final Integer srTimeFrame = position.getSrMatrix().getTimeFrame();

        if (slippage > inst.getMaxSlippage())
        {
            logger.info("Unusual price detected cannot process order slippage = {} , max allowed slippage = {} ", slippage, inst.getMaxSlippage());
            return;
        }

        if (signalBuilder.getCandleBuilder1Min() != null
                && signalBuilder.getCandleBuilder1Min().candles().size() >= 15
                && srTimeFrameUnit != null && srTimeFrame != null)
        {
            final Double rsi = getRsi(signalBuilder);
            final Double atr = getAtr(signalBuilder);
            final Double ema = getEma(signalBuilder);
            CandleBuilder candleBuilder = signalBuilder.getCandleBuilder1Min();
            String candleTimeFrameUnit = candleBuilder.getTimeFrame().getUnit();
            Integer interval = candleBuilder.getInterval();

            if (candleTimeFrameUnit != null
                    && candleTimeFrameUnit.equals(srTimeFrameUnit)
                    && interval != null && interval.compareTo(srTimeFrame) == 0)
            {
                if (rsi != null)
                {
                    if (rsi <= 33 && ask > ema)
                    {
                        logger.info("Placing buy order timeframe 1 min extreme sold condition... rsi = {} , atr = {} ema = {} ", rsi, atr, ema);
                        Double tp = ask + 10;
                        Double sl = ask - 10;
                        createAndSaveMarketOrder((buildBuyOrder(instrumentRate, maxSlippage, tp, sl, //
                                position, inst, leverage, "TimeFrame = 1 min tp = " + tp + " sl = " + sl + " rsi = " + rsi + " atr = " + atr + " ema = " + ema)));
                        return;
                    }
//                    if (rsi >= 50 && rsi <= 60)
//                    {
//                        Double tp = ask + 10;
//                        Double sl = ask - 10;
//                        logger.info("Placing buy order timeframe 1 min bull condition condition... rsi = {} , atr = {} ema = {} ", rsi, atr, ema);
//
//                        createAndSaveMarketOrder((buildBuyOrder(instrumentRate, maxSlippage, sl, tp,//
//                                position, inst, leverage, "TimeFrame = 1 min rsi = " + rsi + " atr = " + atr + " ema = " + ema)));
//                        return;
//                    }
                    if (rsi >= 79 && ask < ema)
                    {
                        logger.info("Placing sell order timeframe 1 min bear condition condition... rsi = {} , atr = {} ema = {} ", rsi, atr, ema);
                        Double tp = ask - 10;
                        Double sl = ask + 10;
                        String orderInfo = "TimeFrame = 1 min tp = " + tp + " sl = " + sl + " rsi = " + rsi + " atr = " + atr + " ema = " + ema;
                        createAndSaveMarketOrder((buildSellOrderDynamicSlTp(instrumentRate, sl, tp, position, inst, orderInfo)));
                    }
                }
            }
        }
    }

    private void processFourHourTimeFrame(LiveInstrumentRate instrumentRate, Position position, Instrument inst)
    {
        final Double ask = instrumentRate.getAsk();
        final Double bid = instrumentRate.getBid();
        final Double maxSlippage = inst.getMaxSlippage();
        final Integer leverage = position.getConfiguration().getLev();
        final Double support = position.getSrMatrix().getSupport();
        final Double resistance = position.getSrMatrix().getResistance();

        if (instrumentRate.getAsk() <= support)
        {
            logger.info("Buy order successfully placed for timeframe 4 hour");
            Double sl = ask - position.getStopLoss();
            Double tp = ask + position.getTakeProfit();
            createAndSaveMarketOrder((buildBuyOrder(instrumentRate, maxSlippage,
                    tp, sl, //
                    position, inst, leverage, "Timeframe = 1 hour , support = " + support + " Resistance = " + resistance + " " +
                            "bid = " + bid + "ask = " + ask + " SL = " + sl + " TP = " + tp)));
            return;
        }

        if (instrumentRate.getAsk() >= resistance) //
        {
            Double sl = ask + position.getStopLoss();
            Double tp = ask - position.getTakeProfit();
            logger.info("Sell order successfully placed for timeframe 4 hour");
            createAndSaveMarketOrder((buildSellOrder(instrumentRate, sl,
                    tp, position, inst, "Timeframe = 1 hour , support = " + support + " Resistance = " + resistance + " " +
                            "bid = " + bid + "ask = " + ask + " SL = " + sl + " TP = " + tp)));
        }
    }

    private void processOneHourTimeFrame(LiveInstrumentRate instrumentRate, Double maxSlippage, Double ask, Double bid, Position position, Instrument inst, Integer leverage, double support, double resistance)
    {
        if (instrumentRate.getAsk() <= support)
        {
            logger.info("Buy order successfully placed for timeframe 1 hour");
            Double sl = ask - position.getStopLoss();
            Double tp = ask + position.getTakeProfit();
            String orderInfo = "Timeframe = 1 hour , support = " + support + " Resistance = " + resistance + " " +
                    "bid = " + bid + "ask = " + ask + " SL = " + sl + " TP = " + tp;
            createAndSaveMarketOrder((buildBuyOrder(instrumentRate, maxSlippage,
                    tp, sl, //
                    position, inst, leverage, orderInfo)));
        }

        if (instrumentRate.getAsk() >= resistance) //
        {
            logger.info("Sell order successfully placed for timeframe 1 hour");
            Double sl = ask + position.getStopLoss();
            Double tp = ask - position.getTakeProfit();
            String orderInfo = "Timeframe = 1 hour , support = " + support + " Resistance = " + resistance + " " +
                    "bid = " + bid + "ask = " + ask + " SL = " + sl + " TP = " + tp;
            createAndSaveMarketOrder((buildSellOrder(instrumentRate, sl, tp, position, inst,
                    orderInfo)));
        }
    }

    private static Double getEma(SignalBuilder signalBuilder)
    {
        return signalBuilder.getCandleBuilder1Min().getEma() != null ?
                signalBuilder.getCandleBuilder1Min().getEma().getEma() : null;
    }

    private static Double getAtr(SignalBuilder signalBuilder)
    {
        return signalBuilder.getCandleBuilder1Min().getAtr() != null ?
                signalBuilder.getCandleBuilder1Min().getAtr().getCurrentATR() : null;
    }

    private static Double getRsi(SignalBuilder signalBuilder)
    {
        return signalBuilder.getCandleBuilder1Min().getRsi() != null ?
                signalBuilder.getCandleBuilder1Min().getRsi().getRsi() : null;
    }

    private static EtoroMarketOrderDto buildSellOrder(LiveInstrumentRate instrumentRate,
                                                      Double sl,
                                                      Double tp,
                                                      Position position,
                                                      Instrument inst,
                                                      String orderInfo)
    {
        Double slippage = instrumentRate.getAsk() - instrumentRate.getBid();
        return EtoroMarketOrderDto.builder().instrumentId(inst.getEtoroInstrumentId()) //
                .isBuy(false)
                .leverage(position.getLeverage())
                .amount(position.getCurrentPositionEquity())//
                .stopLossRate(sl).takeProfitRate(tp)//
                .isTslEnabled(null)
                .isNoTakeProfit(null)
                .isNoStopLoss(null)//
                .orderType(OrderTypes.AUTO.getOrderType())//
                .bid(instrumentRate.getBid()).ask(instrumentRate.getAsk())
                .maxAllowedSlippage(inst.getMaxSlippage()) //
                .etoroSlippage(slippage) //
                .orderInfo(orderInfo)
                .build();
    }


    private static EtoroMarketOrderDto buildSellOrderDynamicSlTp(LiveInstrumentRate instrumentRate, Double sl, Double tp, Position position, Instrument inst,
                                                                 String orderInfo)
    {
        Double slippage = instrumentRate.getAsk() - instrumentRate.getBid();
        return EtoroMarketOrderDto.builder().instrumentId(inst.getEtoroInstrumentId()) //
                .isBuy(false)
                .leverage(position.getLeverage())
                .amount(position.getCurrentPositionEquity())//
                .stopLossRate(sl).takeProfitRate(tp)//
                .isTslEnabled(null)
                .isNoTakeProfit(null)
                .isNoStopLoss(null)//
                .orderType(OrderTypes.AUTO.getOrderType())//
                .bid(instrumentRate.getBid()).ask(instrumentRate.getAsk())
                .maxAllowedSlippage(inst.getMaxSlippage()) //
                .etoroSlippage(slippage) //
                .orderInfo(orderInfo)
                .build();
    }


    private static EtoroMarketOrderDto buildBuyOrder(LiveInstrumentRate instrumentRate,
                                                     Double maxSlippage,
                                                     Double sl,
                                                     Double tp,
                                                     Position position,
                                                     Instrument inst,
                                                     Integer leverage,
                                                     String info)
    {
        Double slippage = instrumentRate.getAsk() - instrumentRate.getBid();
        return EtoroMarketOrderDto.builder()//
                .instrumentId(inst.getEtoroInstrumentId())//
                .isBuy(true)//
                .leverage(leverage)//
                .amount(position.getCurrentPositionEquity())//
                .stopLossRate(sl)//
                .takeProfitRate(tp)//
                .isTslEnabled(null)
                .isNoTakeProfit(null)
                .isNoStopLoss(null)//
                .orderType(OrderTypes.AUTO.getOrderType())//
                .bid(instrumentRate.getBid())//
                .ask(instrumentRate.getAsk())//
                .maxAllowedSlippage(maxSlippage)//
                .etoroSlippage(slippage)
                .orderInfo(info)
                .build();
    }
}
