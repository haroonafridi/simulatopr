package com.hkcapital.portoflio.service.impl.etoro;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portoflio.etoro.dto.portfolio.EtoroPortfolioPositionDTO;
import com.hkcapital.portoflio.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portoflio.etoro.master.Instruments;
import com.hkcapital.portoflio.order.EtoroOrder;
import com.hkcapital.portoflio.repository.OrderRepository;
import com.hkcapital.portoflio.service.OrderManagerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto.createDummyOrderBtc;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Transactional
public class EtoroOrderManagerServiceImplTest
{
    private static final ObjectMapper mapper = new ObjectMapper();

    private OrderManagerService orderManagerService;


    private OrderRepository orderRepository;

    @Autowired
    public EtoroOrderManagerServiceImplTest(final OrderManagerService orderManagerService,
                                            final OrderRepository orderRepository)
    {
        this.orderManagerService = orderManagerService;
        this.orderRepository = orderRepository;
    }

    @Value("classpath:portfolio.json")
    private Resource portfolio;

    @BeforeAll
    public static void beforAll()
    {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    void createMarketOrder()
    {

    }

    @Test
    void createLimitOrder()
    {
    }

    @Test
    void createOrder()
    {
    }

    @Test
    void getOrderInformation()
    {
    }

    @Test
    void createAndSaveMarketOrder()
    {
    }

    @Test
    void etoroPortfolio()
    {
    }

    @Test
    void testEtoroPortfolioResponseDTO() throws IOException
    {


        String jsonString = new String(
                FileCopyUtils.copyToByteArray(portfolio.getInputStream()),
                StandardCharsets.UTF_8
        );

        EtoroPortfolioResponseDTO portfolio = mapper.readValue(jsonString,
                EtoroPortfolioResponseDTO.class);
        Assertions.assertNotNull(portfolio);
        Assertions.assertNotNull(portfolio.getClientPortfolio());
        assertTrue(portfolio.getClientPortfolio().getPositions().size() == 1);
        assertTrue(portfolio.getClientPortfolio().getPositions().size() == 1);
        List<EtoroPortfolioPositionDTO> positions = portfolio.getClientPortfolio().getPositions();
        EtoroPortfolioPositionDTO etoroPortfolioPositionDTO = positions.stream().findFirst().get();
        Assertions.assertNotNull(etoroPortfolioPositionDTO);

        assertEquals(3440592011l, etoroPortfolioPositionDTO.getPositionId());
        assertEquals(15126196, etoroPortfolioPositionDTO.getCid());
        assertEquals("2026-02-20T20:10:44.093Z", etoroPortfolioPositionDTO.getOpenDateTime().toString());

        assertEquals(25006.94d, etoroPortfolioPositionDTO.getOpenRate());
        assertEquals(28, etoroPortfolioPositionDTO.getInstrumentId());
        assertTrue(etoroPortfolioPositionDTO.isBuy());
        assertTrue(etoroPortfolioPositionDTO.isBuy());
        assertEquals(37510.69d, etoroPortfolioPositionDTO.getTakeProfitRate());
        assertEquals(24381.76d, etoroPortfolioPositionDTO.getStopLossRate());
        assertEquals(0, etoroPortfolioPositionDTO.getMirrorId());
        assertEquals(0, etoroPortfolioPositionDTO.getParentPositionID());
        assertEquals(50, etoroPortfolioPositionDTO.getAmount());
        assertEquals(20, etoroPortfolioPositionDTO.getLeverage());
        assertEquals(329711331l, etoroPortfolioPositionDTO.getOrderId());
        assertEquals(17, etoroPortfolioPositionDTO.getOrderType());
        assertEquals(0.039988, etoroPortfolioPositionDTO.getUnits());
        assertEquals(0, etoroPortfolioPositionDTO.getTotalFees());
        assertEquals(50, etoroPortfolioPositionDTO.getInitialAmountInDollars());
        Assertions.assertFalse(etoroPortfolioPositionDTO.isTslEnabled());
        assertEquals(1, etoroPortfolioPositionDTO.getStopLossVersion());
        Assertions.assertFalse(etoroPortfolioPositionDTO.isSettled());
        assertEquals(0, etoroPortfolioPositionDTO.getRedeemStatusId());
        assertEquals(0.039988, etoroPortfolioPositionDTO.getInitialUnits());
        Assertions.assertFalse(etoroPortfolioPositionDTO.isPartiallyAltered());
        assertEquals(50, etoroPortfolioPositionDTO.getUnitsBaseValueDollars());
        Assertions.assertFalse(etoroPortfolioPositionDTO.isDiscounted());
        assertEquals(0, etoroPortfolioPositionDTO.getOpenPositionActionType());
        assertEquals(0, etoroPortfolioPositionDTO.getSettlementTypeId());
        Assertions.assertFalse(etoroPortfolioPositionDTO.isDetached());
        assertEquals(1, etoroPortfolioPositionDTO.getOpenConversionRate());
        assertEquals(0, etoroPortfolioPositionDTO.getPnlVersion());
        assertEquals(0, etoroPortfolioPositionDTO.getTotalFees());
        assertEquals(0, etoroPortfolioPositionDTO.getTotalExternalFees());
        Assertions.assertFalse(etoroPortfolioPositionDTO.isNoTakeProfit());
        Assertions.assertFalse(etoroPortfolioPositionDTO.isNoStopLoss());
        assertEquals(0.039988, etoroPortfolioPositionDTO.getLotCount());
        assertTrue(portfolio.getClientPortfolio().getMirrors().isEmpty());
        assertEquals(213725.12, portfolio.getClientPortfolio().getCredit());
        assertTrue(portfolio.getClientPortfolio().getOrders().isEmpty());
        assertTrue(portfolio.getClientPortfolio().getStockOrders().isEmpty());
        assertTrue(portfolio.getClientPortfolio().getEntryOrders().isEmpty());
        assertTrue(portfolio.getClientPortfolio().getExitOrders().isEmpty());
        assertTrue(portfolio.getClientPortfolio().getOrdersForOpen().isEmpty());
        assertTrue(portfolio.getClientPortfolio().getOrdersForClose().isEmpty());
        assertTrue(portfolio.getClientPortfolio().getOrdersForCloseMultiple().isEmpty());
        assertEquals(0, portfolio.getClientPortfolio().getBonusCredit());
    }

    @Test
    @Commit
    void saveOrder()
    {
        EtoroOrderDetails orderDetails = new EtoroOrderDetails();
        orderDetails.setOrderID(11111);
        orderDetails.setAmount(50);
        orderDetails.setLeverage(50);
        orderDetails.setInstrumentID(Instruments.BTC.getInstrumentId());
        orderDetails.setTakeProfitRate(5d);
        String etoroOrderToken = "xxxxyyyyzzzzaaaabbbb";
        EtoroOrder etoroOrder = orderManagerService.saveOrder(createDummyOrderBtc(), orderDetails, etoroOrderToken);
        System.out.println(etoroOrder);
    }
}