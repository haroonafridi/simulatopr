package com.hkcapital.portflio.service.orders.impl.etoro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portflio.broker.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portflio.broker.etoro.dto.order.EtoroOrderDetailsResponseDTO;
import com.hkcapital.portflio.broker.etoro.dto.portfolio.EtoroPortfolioPositionDTO;
import com.hkcapital.portflio.broker.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portflio.market.indicators.CandleBuilder;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketAction;
import com.hkcapital.portflio.market.structure.MarketStructureCache;
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
import com.hkcapital.portflio.service.orders.*;
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
    private final MarketStructureCache marketStructureCache;

    public EtoroOrderManagerServiceImpl(final EtoroOrderRepository orderRepo, //
                                        final EtoroApiService etoroApiService,
                                        final InstrumentService instrumentService,
                                        final StrategyService strategyService,
                                        final PositionService positionService,
                                        final MarketStructureCache marketStructureCache)
    {
        this.orderRepository = orderRepo;
        this.etoroApiService = etoroApiService;
        this.instrumentService = instrumentService;
        this.strategyService = strategyService;
        this.positionService = positionService;
        this.marketStructureCache = marketStructureCache;
    }

    @Override
    public void process(LiveInstrumentRate instrumentRate, SignalBuilder signalBuilder)
    {
        logger.info("Sending order to etoro!!");
        TimeFrameOrderProcessor orderProcessor =
                new TimeFrameOrderProcessorImpl(instrumentService,strategyService,
                        positionService, marketStructureCache,this);
        orderProcessor.process(instrumentRate,signalBuilder);
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
                    orderRepository.findByInstrumentIDAndOderTypeAndStatusAndTimeFrameAndTimeFrameUnit( //
                            etoroMarketOrderDto.getInstrumentId(), //
                            OrderTypes.AUTO.getOrderType(),
                            OrderStatus.SENT.getOrderStatus(),
                            etoroMarketOrderDto.getTimeFrame().timeFrame(),
                            etoroMarketOrderDto.getTimeFrame().timeFrameUnit()); //

            if (orders.size() > 0)
            {
                logger.error("open order already exist for instrument  {}", etoroMarketOrderDto.getInstrumentId());
                return null;
            }

            final EtoroOrderDetailsResponseDTO orderResponse = //
                    etoroApiService.createMarketOrder(etoroMarketOrderDto);

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
        etoroOrder.fill(orderDetails, etoroMarketOrderDto.getTimeFrame());
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
    public EtoroOrder findByorderID(Long orderId)
    {
        return orderRepository.findByorderID(orderId);
    }

    @Override
    public EtoroOrder closeEtoroOrder(EtoroOrder etoroOrder)
    {
        return orderRepository.save(etoroOrder);
    }

    @Override
    public EtoroOrder addEtoroOrder(EtoroOrder etoroOrder)
    {
        return orderRepository.save(etoroOrder);
    }


}
