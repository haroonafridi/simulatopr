package com.hkcapital.portoflio.service.orders.impl.etoro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkcapital.portoflio.broker.etoro.EtoroOrderException;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroOrderDetailsResponseDTO;
import com.hkcapital.portoflio.broker.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portoflio.model.etoro.EtoroOrder;
import com.hkcapital.portoflio.repository.orders.etoro.EtoroOrderRepository;
import com.hkcapital.portoflio.service.api.etoro.EtoroApiService;
import com.hkcapital.portoflio.service.orders.OrderManagerService;
import com.hkcapital.portoflio.values.order.OrderStatus;
import com.hkcapital.portoflio.values.order.OrderTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

    public EtoroOrderManagerServiceImpl(final EtoroOrderRepository orderRepository, //
                                        final EtoroApiService etoroApiService)
    {
        this.orderRepository = orderRepository;
        this.etoroApiService = etoroApiService;
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
                throw new EtoroOrderException("Open order already exist for instrument id = " + etoroMarketOrderDto.getInstrumentId());
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

}
