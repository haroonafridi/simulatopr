package com.hkcapital.portoflio.service.orders.impl.etoro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkcapital.portoflio.broker.etoro.EtoroOrderException;
import com.hkcapital.portoflio.broker.etoro.config.Configuration;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroOrderDetailsResponseDTO;
import com.hkcapital.portoflio.broker.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.model.SRMatrix;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.model.etoro.EtoroOrder;
import com.hkcapital.portoflio.repository.orders.etoro.EtoroOrderRepository;
import com.hkcapital.portoflio.service.api.etoro.EtoroApiService;
import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portoflio.service.candle.etoro.impl.SignalBuilder;
import com.hkcapital.portoflio.service.instrument.InstrumentService;
import com.hkcapital.portoflio.service.orders.OrderManagerService;
import com.hkcapital.portoflio.service.positions.PositionService;
import com.hkcapital.portoflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portoflio.service.strategy.StrategyService;
import com.hkcapital.portoflio.values.order.OrderStatus;
import com.hkcapital.portoflio.values.order.OrderTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final SRMatrixService srMatrixService;
    private final PositionService positionService;
    public EtoroOrderManagerServiceImpl(final EtoroOrderRepository orderRepository, //
                                        final EtoroApiService etoroApiService,
                                        final InstrumentService instrumentService,
                                        final StrategyService strategyService,
                                        final SRMatrixService srMatrixService,
                                        final PositionService positionService)
    {
        this.orderRepository = orderRepository;
        this.etoroApiService = etoroApiService;
        this.instrumentService = instrumentService;
        this.strategyService = strategyService;
        this.srMatrixService = srMatrixService;
        this.positionService = positionService;
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

            final EtoroOrderDetails orderDetails = orderResponse.getOrderForOpen();

            return saveOrder(etoroMarketOrderDto, orderDetails, orderResponse.getToken());

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
                .mapToLong(position -> position.getOrderId()) //
                .boxed() //
                .collect(Collectors.toList());

        orderRepository.findAll().forEach(order ->  //
        {
            long existedOrder = openPositions.stream().filter(o -> o.longValue() == order.getOrderID()).count();

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
        Instrument instrument = instrumentService.findAll().stream().filter(e -> e.getActive()).findAny()//
                .get();

        Double maxSlippage = instrument.getMaxSlippage();

        if (instrumentRate != null && instrumentRate.getAsk() != null && instrumentRate.getBid() != null
                && instrument.getEtoroInstrumentId().intValue() == instrumentRate.getInstrumentId().intValue())
        {
            if (Configuration.ACTIVATE_AUTOMATIC_TRADING)
            {
                logger.info("Sending Automatic trade to etoro!");
                Double ask = instrumentRate.getAsk();
                Double bid = instrumentRate.getBid();
                Double slippage = ask - bid;
                if (slippage > maxSlippage)
                {
                    logger.info("Unusual price received, Order rejected due to high slippage,  bid = [{}] , ask = [{}], slippage = [{}] , maxSlippage = [{}]", bid, ask, slippage, maxSlippage);
                    return;
                }
                logger.info("Instrument price received bid = [{}] , ask = [{}] slippage = [{}] , maxSlippage = [{}] sending order for execution", bid, ask, slippage, maxSlippage);

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
                        if(signalBuilder !=null && inst.getActive())
                        {
                            logger.info("No of candles generated {} ", signalBuilder.getCandleBuilder1Min().candles().size());
                            if(signalBuilder.getCandleBuilder1Min() != null && signalBuilder.getCandleBuilder1Min().candles().size() >=15 )
                            {
                                Double rsi = signalBuilder.getCandleBuilder1Min().getRsi() != null ? signalBuilder.getCandleBuilder1Min().getRsi().getRsi() : null;
                                Double atr = signalBuilder.getCandleBuilder1Min().getAtr() != null ? signalBuilder.getCandleBuilder1Min().getAtr().getCurrentATR() : null;
                                Double ema = signalBuilder.getCandleBuilder1Min().getEma() != null ? signalBuilder.getCandleBuilder1Min().getEma().getEma() : null;

                                if (rsi <= 15) {
                                    logger.info("Placing buy order timeframe 1 min extreme sold condition... rsi = {} , atr = {} ema = {} ", rsi, atr, ema);
                                    EtoroMarketOrderDto etoroMarketBuyOrder = EtoroMarketOrderDto.builder().instrumentId(inst.getEtoroInstrumentId()).isBuy(true).leverage(leverage).amount(position.getCurrentPositionEquity()).stopLossRate(null)
                                            .takeProfitRate(instrumentRate.getAsk() + 100).isTslEnabled(null).isNoTakeProfit(null).isNoStopLoss(null).orderType(OrderTypes.AUTO.getOrderType()).bid(bid).ask(ask).maxAllowedSlippage(maxSlippage).etoroSlippage(slippage).build();
                                    createAndSaveMarketOrder((etoroMarketBuyOrder));
                                    return;
                                }
                                if (rsi >= 50 && rsi <=60) {
                                    logger.info("Placing buy order timeframe 1 min bull condition condition... rsi = {} , atr = {} ema = {} ", rsi, atr, ema);
                                    EtoroMarketOrderDto etoroMarketBuyOrder = EtoroMarketOrderDto.builder().instrumentId(inst.getEtoroInstrumentId()).isBuy(true).leverage(leverage).amount(position.getCurrentPositionEquity()).stopLossRate(null).takeProfitRate(instrumentRate.getAsk()+100).isTslEnabled(null).isNoTakeProfit(null).isNoStopLoss(null).orderType(OrderTypes.AUTO.getOrderType()).bid(bid).ask(ask).maxAllowedSlippage(maxSlippage).etoroSlippage(slippage).build();
                                    createAndSaveMarketOrder((etoroMarketBuyOrder));
                                    return;
                                }
                                if (rsi <= 40) {
                                    logger.info("Placing buy order timeframe 1 min bear condition condition... rsi = {} , atr = {} ema = {} ", rsi, atr, ema);
                                    EtoroMarketOrderDto etoroMarketSellOrder = EtoroMarketOrderDto.builder().instrumentId(inst.getEtoroInstrumentId()).isBuy(false).leverage(leverage).amount(position.getCurrentPositionEquity()).stopLossRate(null).takeProfitRate(instrumentRate.getAsk()-100).isTslEnabled(null).isNoTakeProfit(null).isNoStopLoss(null).orderType(OrderTypes.AUTO.getOrderType()).bid(bid).ask(ask).maxAllowedSlippage(maxSlippage).etoroSlippage(slippage).build();
                                    createAndSaveMarketOrder((etoroMarketSellOrder));
                                    return;
                                }
                            }
                        }

                    }
                }
            }

        }
    }
}
