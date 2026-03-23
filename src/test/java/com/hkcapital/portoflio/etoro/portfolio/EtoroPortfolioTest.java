package com.hkcapital.portoflio.etoro.portfolio;

import com.hkcapital.portoflio.etoro.EtoroAbstractTest;
import com.hkcapital.portoflio.etoro.dto.portfolio.EtoroClientPortfolioDTO;
import com.hkcapital.portoflio.etoro.dto.portfolio.EtoroPortfolioPositionDTO;
import com.hkcapital.portoflio.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EtoroPortfolioTest extends EtoroAbstractTest
{
    private final static String PORTFOLIO_DATA = //
            "D:/portfolio-pnl-simulator/src/test/data/portfolio/portfolio.json";
    @Test
    public void testPorfolioPositions() throws Exception
    {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(PORTFOLIO_DATA)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                content.append(line).append("\n");
            }
        }
        EtoroPortfolioResponseDTO portolioResponse = objectMapper.readValue(content.toString(),
                EtoroPortfolioResponseDTO.class);
        EtoroClientPortfolioDTO etoroClientPortfolioDTO = portolioResponse.getClientPortfolio();
        List<EtoroPortfolioPositionDTO> positions = etoroClientPortfolioDTO.getPositions();
        assertEquals(3, positions.size());
        assertGoldPosition(positions);
        assertNasdaq100Position(positions);
        assertJPNNikkiPosition(positions);
    }

    private static void assertJPNNikkiPosition(List<EtoroPortfolioPositionDTO> positions)
    {
        EtoroPortfolioPositionDTO positionDTO2 = positions.get(2);
        System.out.println(positionDTO2);
        assertEquals(3472201380L, positionDTO2.getPositionId());
        assertEquals(15126196, positionDTO2.getCid());
        assertEquals(OffsetDateTime.parse("2026-03-23T18:46:50.383Z"), positionDTO2.getOpenDateTime());
        assertEquals(53518.7d, positionDTO2.getOpenRate());
        assertEquals(36, positionDTO2.getInstrumentId());
        assertTrue(positionDTO2.isBuy());
        assertEquals(54856.67d, positionDTO2.getTakeProfitRate());
        assertEquals(52180.68d, positionDTO2.getStopLossRate());
        assertEquals(0, positionDTO2.getMirrorId());
        assertEquals(0, positionDTO2.getParentPositionID());
        assertEquals(99.99d, positionDTO2.getAmount());
        assertEquals(20, positionDTO2.getLeverage());
        assertEquals(337994705, positionDTO2.getOrderId());
        assertEquals(17, positionDTO2.getOrderType());
        assertEquals(5.906727d, positionDTO2.getUnits());
        assertEquals(0.0d, positionDTO2.getTotalFees());
        assertEquals(99.99d, positionDTO2.getInitialAmountInDollars());
        assertFalse(positionDTO2.isTslEnabled());
        assertEquals(1, positionDTO2.getStopLossVersion());
        assertFalse(positionDTO2.isSettled());
        assertEquals(0, positionDTO2.getRedeemStatusId());
        assertEquals(5.906727d, positionDTO2.getInitialUnits());
        assertFalse(positionDTO2.isPartiallyAltered());
        assertEquals(99.99d, positionDTO2.getUnitsBaseValueDollars());
        assertFalse(positionDTO2.isDiscounted());
        assertEquals(0, positionDTO2.getOpenPositionActionType());
        assertEquals(0, positionDTO2.getSettlementTypeId());
        assertFalse(positionDTO2.isDetached());
        assertEquals(0.00632607d, positionDTO2.getOpenConversionRate());
        assertEquals(0, positionDTO2.getPnlVersion());
        assertEquals(0.0d, positionDTO2.getTotalExternalFees());
        assertFalse(positionDTO2.isNoTakeProfit());
        assertFalse(positionDTO2.isNoStopLoss());
        assertEquals(5.906727d, positionDTO2.getLotCount());
    }

    private static void assertNasdaq100Position(List<EtoroPortfolioPositionDTO> positions)
    {
        EtoroPortfolioPositionDTO positionDTO1 = positions.get(1);
        assertEquals(3472201322L, positionDTO1.getPositionId());
        assertEquals(15126196, positionDTO1.getCid());
        assertEquals(OffsetDateTime.parse("2026-03-23T18:46:36.617Z"), positionDTO1.getOpenDateTime());
        assertEquals(24296.98d, positionDTO1.getOpenRate());
        assertEquals(28, positionDTO1.getInstrumentId());
        assertTrue(positionDTO1.isBuy());
        assertEquals(24904.4d, positionDTO1.getTakeProfitRate());
        assertEquals(23689.56d, positionDTO1.getStopLossRate());
        assertEquals(0, positionDTO1.getMirrorId());
        assertEquals(0, positionDTO1.getParentPositionID());
        assertEquals(99.99d, positionDTO1.getAmount());
        assertEquals(20, positionDTO1.getLeverage());
        assertEquals(338018368, positionDTO1.getOrderId());
        assertEquals(17, positionDTO1.getOrderType());
        assertEquals(0.082306d, positionDTO1.getUnits());
        assertEquals(0.0d, positionDTO1.getTotalFees());
        assertEquals(99.99d, positionDTO1.getInitialAmountInDollars());
        assertFalse(positionDTO1.isTslEnabled());
        assertEquals(99.99d, positionDTO1.getInitialAmountInDollars());
        assertFalse(positionDTO1.isSettled());
        assertEquals(0, positionDTO1.getRedeemStatusId());
        assertEquals(0.082306d, positionDTO1.getInitialUnits());
        assertFalse(positionDTO1.isPartiallyAltered());
        assertEquals(99.99d, positionDTO1.getUnitsBaseValueDollars());
        assertFalse(positionDTO1.isDiscounted());
        assertEquals(0, positionDTO1.getOpenPositionActionType());
        assertEquals(0, positionDTO1.getSettlementTypeId());
        assertFalse(positionDTO1.isDetached());
        assertEquals(1.0d, positionDTO1.getOpenConversionRate());
        assertEquals(0, positionDTO1.getPnlVersion());
        assertEquals(0.0d, positionDTO1.getTotalExternalFees());
        assertFalse(positionDTO1.isNoTakeProfit());
        assertFalse(positionDTO1.isNoStopLoss());
        assertEquals(0.082306d, positionDTO1.getLotCount());
    }

    private static void assertGoldPosition(List<EtoroPortfolioPositionDTO> positions)
    {
        EtoroPortfolioPositionDTO positionDTO0 = positions.get(0);
        assertEquals(3472107412L, positionDTO0.getPositionId());
        assertEquals(15126196, positionDTO0.getCid());
        assertEquals(3472107412L, positionDTO0.getPositionId());
        assertEquals(OffsetDateTime.parse("2026-03-23T16:03:58.787Z"), positionDTO0.getOpenDateTime());
        assertEquals(4364.61d, positionDTO0.getOpenRate());
        assertEquals(18, positionDTO0.getInstrumentId());
        assertTrue(positionDTO0.isBuy());
        assertEquals(4473.73d, positionDTO0.getTakeProfitRate());
        assertEquals(4255.49d, positionDTO0.getStopLossRate());
        assertEquals(0, positionDTO0.getMirrorId());
        assertEquals(0, positionDTO0.getParentPositionID());
        assertEquals(99.99d, positionDTO0.getAmount());
        assertEquals(20, positionDTO0.getLeverage());
        assertEquals(337970127, positionDTO0.getOrderId());
        assertEquals(17, positionDTO0.getOrderType());
        assertEquals(0.458197d, positionDTO0.getUnits());
        assertEquals(0.0d, positionDTO0.getTotalFees());
        assertEquals(99.99d, positionDTO0.getInitialAmountInDollars());
        assertFalse(positionDTO0.isTslEnabled());
        assertEquals(99.99d, positionDTO0.getInitialAmountInDollars());
        assertFalse(positionDTO0.isSettled());
        assertEquals(0, positionDTO0.getRedeemStatusId());
        assertEquals(0.458197d, positionDTO0.getInitialUnits());
        assertFalse(positionDTO0.isPartiallyAltered());
        assertEquals(99.99d, positionDTO0.getUnitsBaseValueDollars());
        assertFalse(positionDTO0.isDiscounted());
        assertEquals(0, positionDTO0.getOpenPositionActionType());
        assertEquals(0, positionDTO0.getSettlementTypeId());
        assertFalse(positionDTO0.isDetached());
        assertEquals(1.0d, positionDTO0.getOpenConversionRate());
        assertEquals(0, positionDTO0.getPnlVersion());
        assertEquals(0.0d, positionDTO0.getTotalExternalFees());
        assertFalse(positionDTO0.isNoTakeProfit());
        assertFalse(positionDTO0.isNoStopLoss());
        assertEquals(0.458197d, positionDTO0.getLotCount());
    }
}
