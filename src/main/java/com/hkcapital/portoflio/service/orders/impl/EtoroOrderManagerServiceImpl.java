package com.hkcapital.portoflio.service.orders.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkcapital.portoflio.broker.etoro.EtoroOrderException;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroOrderDetailsResponseDTO;
import com.hkcapital.portoflio.broker.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portoflio.order.EtoroOrder;
import com.hkcapital.portoflio.order.OrderStatus;
import com.hkcapital.portoflio.order.OrderTypes;
import com.hkcapital.portoflio.repository.orders.OrderRepository;
import com.hkcapital.portoflio.service.orders.OrderManagerService;
import com.hkcapital.portoflio.service.api.etoro.EtoroApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtoroOrderManagerServiceImpl implements OrderManagerService
{
    private static final Logger logger = LoggerFactory.getLogger(EtoroOrderManagerServiceImpl.class);
    private final OrderRepository orderRepository;

    private final EtoroApiService etoroApiService;

    public EtoroOrderManagerServiceImpl(OrderRepository orderRepository, EtoroApiService etoroApiService)
    {
        this.orderRepository = orderRepository;
        this.etoroApiService = etoroApiService;
    }

    @Override
    public EtoroOrder createAndSaveMarketOrder(EtoroMarketOrderDto etoroMarketOrderDto)
    {
        logger.info("Send and saving etoro order for instrument [{}]", etoroMarketOrderDto.getInstrumentId());
        try
        {
            List<EtoroOrder> orders = //
                    orderRepository.findByInstrumentIDAndOderTypeAndStatus(etoroMarketOrderDto.getInstrumentId(), //
                            OrderTypes.AUTO.getOrderType(), OrderStatus.SENT.getOrderStatus()); //

            if (orders.size() > 0)
            {
                throw new EtoroOrderException("Open order already exist for instrument id = " + etoroMarketOrderDto.getInstrumentId());
            }

            EtoroOrderDetailsResponseDTO orderResponse = etoroApiService.createMarketOrder(etoroMarketOrderDto);

            EtoroOrderDetails orderDetails = orderResponse.getOrderForOpen();

            return saveOrder(etoroMarketOrderDto, orderDetails, orderResponse.getToken());
        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EtoroOrder saveOrder(EtoroMarketOrderDto etoroMarketOrderDto,
                                EtoroOrderDetails orderDetails,
                                String etoroOrderToken)
    {
        EtoroOrder etoroOrder = new EtoroOrder();
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
    public EtoroOrder closeEtoroOrder(Integer etoroOrderId)
    {
        EtoroOrder order = orderRepository.findById(etoroOrderId).get();
        order.setStatus(OrderStatus.CLOSED.getOrderStatus());
        return orderRepository.save(order);
    }

    @Override
    public List<EtoroOrder> findByInstrumentID(Integer InstrumentID)
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
