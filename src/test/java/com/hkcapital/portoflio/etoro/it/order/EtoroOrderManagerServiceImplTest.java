package com.hkcapital.portoflio.etoro.it.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkcapital.portoflio.etoro.EtoroAbstractTest;
import com.hkcapital.portoflio.etoro.EtoroOrderException;
import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetailsResponseDTO;
import com.hkcapital.portoflio.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portoflio.order.EtoroOrder;
import com.hkcapital.portoflio.order.OrderStatus;
import com.hkcapital.portoflio.order.OrderTypes;
import com.hkcapital.portoflio.repository.OrderRepository;
import com.hkcapital.portoflio.service.OrderManagerService;
import com.hkcapital.portoflio.service.impl.etoro.EtoroApiService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@SpringBootTest
@Transactional
class EtoroOrderManagerServiceImplTest extends EtoroAbstractTest
{
    private static final String PORTFOLIO_POSITIONS = "/data/portfolio/portfolio.json";
    private static final String TOKEN = "acde070d-8c4c-4f0d-9d8a-162843c10333";
    private static Instant instant = Instant.now(Clock.fixed(Instant.parse("2026-12-03T10:15:30.00Z"),
            ZoneId.of("Europe/Paris")));
    @Autowired
    private OrderManagerService orderManagerService;
    @Autowired
    private OrderRepository orderRepository;
    @MockitoBean
    private EtoroApiService etoroApiService;

    @Test
    public void shouldCreateAndAssertAutomaticAndManualEtoroOrders() //
    {
        shouldCreateAndAssertAutomaticGoldOrdersEtoroOrders(AUTO_ORDER_ID_27);
        shouldCreateAndAssertGoldManualMarketOrderEtoroOrders();
        Assertions.assertEquals(1, orderManagerService.findByInstrumentIDAndOderType(GOLD_ID, OrderTypes.MANUAL.getOrderType()).size());
        Assertions.assertEquals(1, orderManagerService.findByInstrumentIDAndOderType(GOLD_ID, OrderTypes.AUTO.getOrderType()).size());
    }

    @DisplayName("Should close manual and automatic gold orders")
    @Test
    public void shouldCloseManualAndAutoMaticGoldEtoroOrders() //
    {
        shouldCreateAndAssertAutomaticGoldOrdersEtoroOrders(AUTO_ORDER_ID_27);
        shouldCreateAndAssertGoldManualMarketOrderEtoroOrders();
        List<EtoroOrder> orderList = orderManagerService.findByInstrumentID(GOLD_ID);
        long sentOrders = orderList.stream().filter(o -> o.getStatus().equals(OrderStatus.SENT.getOrderStatus())).count();
        Assertions.assertEquals(2, sentOrders);
        orderList.forEach(order -> orderManagerService.closeEtoroOrder(order.getId()));
        orderList = orderManagerService.findByInstrumentID(GOLD_ID);
        long closedOrders = orderList.stream().filter(o -> o.getStatus().equals(OrderStatus.CLOSED.getOrderStatus())).count();
        Assertions.assertEquals(2, closedOrders);
    }

    @DisplayName("Closes local orders not found on Etoro and handles API failures ")
    @Test
    public void shouldFetchOrdersFromEtoro_AndCloseThemIfNotAvailableInEtoro() throws IOException //
    {
        shouldCreateAndAssertAutomaticGoldOrdersEtoroOrders(AUTO_ORDER_ID_28);
        shouldCreateAndAssertGoldManualMarketOrderEtoroOrders();
        List<EtoroOrder> orderList = orderManagerService.findByInstrumentID(GOLD_ID);
        long sentOrders = orderList.stream().filter(o -> o.getStatus().equals(OrderStatus.SENT.getOrderStatus())).count();
        Assertions.assertEquals(2, sentOrders);
        Mockito.when(etoroApiService.etoroPortfolio()).then((invocation)->loadPortfolio(PORTFOLIO_POSITIONS));
        orderManagerService.fetchAndCloseEtoroOrder();
        orderRepository.findAll().forEach(order -> {
            System.out.println("order => "+order.getStatus());
        });
    }

    @Test
    public void testCreateAndSaveMarketOrder() throws IOException
    {
        EtoroMarketOrderDto etoroMarketOrderDto = getEtoroMarketOrderDto();
        EtoroOrderDetailsResponseDTO etoroOrderDetailsResponseDTO = new EtoroOrderDetailsResponseDTO();
        etoroOrderDetailsResponseDTO.setOrderForOpen(getEtoroOrderDetails(instant));
        Mockito.when(etoroApiService.createMarketOrder(etoroMarketOrderDto)).then((a) -> etoroOrderDetailsResponseDTO);
        orderManagerService.createAndSaveMarketOrder(etoroMarketOrderDto);
        assertAutoOrderEtoro(orderRepository.findByorderID(337970127L));
    }

    @Test
    public void createAutoOrder()
    {
        EtoroMarketOrderDto etoroMarketOrderDto = getEtoroMarketOrderDto();
        EtoroOrderDetails orderDetails = getEtoroOrderDetails(instant);
        orderManagerService.saveOrder(etoroMarketOrderDto, //
                orderDetails, //
                TOKEN); //
        assertAutoOrderEtoro(orderRepository.findByorderID(337970127L));
    }

    @DisplayName("Should not create automatic Etoro Gold order when an automatic order already exists")
    @Test
    public void shouldNotCreateAutomaticEtoroGoldOrder_WhenAnAutomaticOrderAlreadyExists() throws JsonProcessingException
    {
        //GIVEN an existing automatic Etoro Gold order
        EtoroMarketOrderDto etoroMarketOrderDto1 = getEtoroMarketOrderDto();
        EtoroOrderDetailsResponseDTO etoroOrderDetailsResponseDTO1 = new EtoroOrderDetailsResponseDTO();
        etoroOrderDetailsResponseDTO1.setOrderForOpen(getEtoroOrderDetails(instant));
        Mockito.when(etoroApiService.createMarketOrder(etoroMarketOrderDto1)).then(invocation -> etoroOrderDetailsResponseDTO1);
        // WHEN creating the first order
        orderManagerService.createAndSaveMarketOrder(etoroMarketOrderDto1);
        //THEN it is saved successfully
        assertAutoOrderEtoro(orderRepository.findByorderID(337970127L));
        //GIVE another automatic order
        EtoroMarketOrderDto etoroMarketOrderDto2 = getEtoroMarketOrderDto();
        EtoroOrderDetailsResponseDTO etoroOrderDetailsResponseDTO2 = new EtoroOrderDetailsResponseDTO();
        etoroOrderDetailsResponseDTO2.setOrderForOpen(getEtoroOrderDetails(instant));
        Mockito.when(etoroApiService.createMarketOrder(etoroMarketOrderDto2)).then(invocation -> etoroOrderDetailsResponseDTO2);
        //WHEN trying to create another automatic order
        //THEN it throws EtoroOrderException
        Assertions.assertThrows(EtoroOrderException.class,
                () -> orderManagerService.createAndSaveMarketOrder(etoroMarketOrderDto2),
                "Open order already exist for instrument id = 18");
    }

    private void assertAutoOrderEtoro(EtoroOrder persistedOrder)
    {
        Assertions.assertEquals(337970127L, persistedOrder.getOrderID());
        Assertions.assertEquals(99.99, persistedOrder.getAmount());
        Assertions.assertEquals(5405, persistedOrder.getAsk());
        Assertions.assertEquals(5400, persistedOrder.getBid());
        Assertions.assertEquals(4255.49, persistedOrder.getStopLossRate());
        Assertions.assertEquals(1.75, persistedOrder.getMaxAllowedSlippage());
        Assertions.assertEquals(20d, persistedOrder.getLeverage());
        Assertions.assertTrue(persistedOrder.isBuy());
        Assertions.assertEquals(instant, persistedOrder.getOpenDateTime());
    }

}