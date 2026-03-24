package com.hkcapital.portoflio.etoro.it.order;


import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portoflio.order.OrderTypes;
import com.hkcapital.portoflio.repository.OrderRepository;
import com.hkcapital.portoflio.service.OrderManagerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class OrderTest
{
    private static final double BID = 5400;
    private static final double ASK = 5400;

    private static final int INSTRUMENT_ID = 18; //GOLD
    private static final int LEVERAGE_20 = 20;
    private static final int ORDER_ID = 10000;
    private static final double STOP_LOSS_RATE = 5400;
    private static final double TAKE_PROFIT_RATE = 5400;
    private static final double ETORO_SLIPPAGE = 0.75d;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderManagerService orderManagerService;

    @Test
    public void testOrderCreation()
    {
        orderRepository.deleteAll();
        Assertions.assertEquals(0, orderRepository.findAll().size());
        EtoroMarketOrderDto etoroMarketOrderDto = EtoroMarketOrderDto
                .builder()
                .bid(BID)
                .ask(ASK)
                .instrumentId(INSTRUMENT_ID)
                .isBuy(Boolean.TRUE)
                .isTslEnabled(Boolean.FALSE)
                .leverage(LEVERAGE_20)
                .stopLossRate(STOP_LOSS_RATE)
                .takeProfitRate(TAKE_PROFIT_RATE)
                .etoroSlippage(ETORO_SLIPPAGE)
                .orderType(OrderTypes.AUTO.getOrderType())
                .amount(100d)
                .build();
        EtoroOrderDetails etoroOrderDetails = new EtoroOrderDetails();
        etoroOrderDetails.setAmount(100d);
        etoroOrderDetails.setInstrumentID(INSTRUMENT_ID);
        etoroOrderDetails.setBuy(Boolean.TRUE);
        etoroOrderDetails.setLeverage(LEVERAGE_20);
        etoroOrderDetails.setStopLossRate(STOP_LOSS_RATE);
        etoroOrderDetails.setTakeProfitRate(TAKE_PROFIT_RATE);
        etoroOrderDetails.setOrderID(ORDER_ID);
        orderManagerService.saveOrder(etoroMarketOrderDto, //
                etoroOrderDetails, //
                UUID.randomUUID().toString());
    }
}
