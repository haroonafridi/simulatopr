package com.hkcapital.portoflio.etoro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portoflio.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portoflio.order.EtoroOrder;
import com.hkcapital.portoflio.order.OrderTypes;
import com.hkcapital.portoflio.repository.OrderRepository;
import com.hkcapital.portoflio.service.OrderManagerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@Import({ObjectMapper.class})
public abstract class EtoroAbstractTest
{
    public static final int GOLD_ID = ETORO_INSTUMENTS.GOLD.getInstumentId();

    public static final int AUTO_ORDER_ID_27 = 337970127;
    public static final int AUTO_ORDER_ID_28 = 337970128;
    public static final int MANUAL_ORDER_ID = 137970199;
    protected String expectedText = "{\"messages\":[{";
    protected ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    @Autowired
    protected OrderManagerService orderManagerService;

    @Autowired
    protected OrderRepository orderRepository;


    public InputStream loadResource(String path)
    {
        return getClass().getResourceAsStream(path);
    }

    public EtoroPortfolioResponseDTO loadPortfolio(String path) throws IOException
    {
        return objectMapper.readValue(new String(loadResource(path).readAllBytes(), //
                        StandardCharsets.UTF_8),
                EtoroPortfolioResponseDTO.class);
    }


    protected void shouldCreateAndAssertGoldManualMarketOrderEtoroOrders()
    {
        orderManagerService.saveOrder(createManualMarketOrderDto(), createManualMarketOrderDetails(), //
                UUID.randomUUID().toString());
        EtoroOrder manualOrder = orderRepository.findByorderID((long) MANUAL_ORDER_ID);
        Assertions.assertNotNull(manualOrder);
    }

    protected static EtoroOrderDetails createManualMarketOrderDetails()
    {
        return EtoroOrderDetails.builder().instrumentID(GOLD_ID)//
                .amount(99.99d).openDateTime(MANUAL_ORDER_OPEN_TIME) //
                .isBuy(true).leverage(20).orderID(MANUAL_ORDER_ID)//
                .build();
    }

    protected static EtoroMarketOrderDto createManualMarketOrderDto()
    {
        return EtoroMarketOrderDto.builder().bid(BID)
                .ask(ASK).instrumentId(GOLD_ID).isBuy(true).isTslEnabled(false)
                .leverage(LEVERAGE_20).stopLossRate(STOP_LOSS_RATE).takeProfitRate(TAKE_PROFIT_RATE)
                .etoroSlippage(ETORO_SLIPPAGE).orderType(OrderTypes.MANUAL.getOrderType()).amount(99.99d)
                .build();
    }

    protected void shouldCreateAndAssertAutomaticGoldOrdersEtoroOrders(int orderId)
    {
        orderManagerService.saveOrder(createAutoMarketOrderDto(),
                createAutoOrderDetails(orderId), UUID.randomUUID().toString());
        EtoroOrder autoOrder = orderRepository.findByorderID((long) orderId);
        Assertions.assertNotNull(autoOrder);
    }

    protected static EtoroOrderDetails createAutoOrderDetails(int orderId)
    {
        return EtoroOrderDetails.builder().instrumentID(GOLD_ID)//
                .amount(99.99d).openDateTime(AUTOMATIC_ORDER_OPEN_TIME) //
                .isBuy(true).leverage(20).orderID(orderId)//
                .build();
    }

    protected static EtoroMarketOrderDto createAutoMarketOrderDto()
    {
        return EtoroMarketOrderDto.builder().bid(BID)
                .ask(ASK).instrumentId(GOLD_ID).isBuy(true).isTslEnabled(false)
                .leverage(LEVERAGE_20).stopLossRate(STOP_LOSS_RATE).takeProfitRate(TAKE_PROFIT_RATE)
                .etoroSlippage(ETORO_SLIPPAGE)
                .orderType(OrderTypes.AUTO.getOrderType()).amount(99.99d)
                .build();
    }


    public static EtoroMarketOrderDto getEtoroMarketOrderDto()
    {
        EtoroMarketOrderDto etoroMarketOrderDto = EtoroMarketOrderDto.builder()
                .orderType(OrderTypes.AUTO.getOrderType())
                .bid(5400d)
                .ask(5405d)
                .instrumentId(18)
                .stopLossRate(4255.49)
                .amount(99.99)
                .maxAllowedSlippage(1.75)
                .isNoTakeProfit(true)
                .leverage(20)
                .isBuy(true)
                .build();
        return etoroMarketOrderDto;
    }

    public static EtoroOrderDetails getEtoroOrderDetails(Instant instant)
    {
        EtoroOrderDetails orderDetails = EtoroOrderDetails.builder()
                .instrumentID(18) // gold
                .orderID(337970127L)
                .openDateTime(instant)
                .isBuy(true) //
                .amount(99.99) //
                .isTslEnabled(false) //
                .stopLossRate(4255.49) //
                .takeProfitRate(4473.73) //
                .leverage(20)
                .build();
        return orderDetails;
    }
    private static final double BID = 5400.01;
    private static final double ASK = 5398.25;
    private static final int LEVERAGE_20 = 20;

    private static final double STOP_LOSS_RATE = 4255.49;
    private static final double TAKE_PROFIT_RATE = 4473.73;
    private static final double ETORO_SLIPPAGE = 0.75d;

    private static final Instant AUTOMATIC_ORDER_OPEN_TIME =
            Instant.parse("2026-01-01T10:00:00Z");

    private static final Instant MANUAL_ORDER_OPEN_TIME =
            Instant.parse("2026-02-01T10:00:00Z");
}
