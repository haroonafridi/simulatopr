package com.hkcapital.portoflio.etoro.portfolio;
import com.hkcapital.portoflio.etoro.EtoroAbstractTest;
import com.hkcapital.portoflio.broker.etoro.dto.portfolio.EtoroClientPortfolioDTO;
import com.hkcapital.portoflio.broker.etoro.dto.portfolio.EtoroPortfolioPositionDTO;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author : Haroon Ur Rasheed Afridi
 * Test for verifying etoro portfolio positions
 */
public class EtoroPortfolioTest extends EtoroAbstractTest
{
    private static final String PORTFOLIO_POSITIONS = "/data/portfolio/portfolio.json";

    @Test
    public void testPortfolioPositions() throws Exception
    {
        EtoroClientPortfolioDTO etoroClientPortfolioDTO = loadPortfolio(PORTFOLIO_POSITIONS).getClientPortfolio();
        List<EtoroPortfolioPositionDTO> positions = etoroClientPortfolioDTO.getPositions();
        assertEquals(3, positions.size());
        assertGoldPosition(positions.get(0));
        assertNasdaq100Position(positions.get(1));
        assertJPNNikkiPosition(positions.get(2));
    }

    private static void assertJPNNikkiPosition(EtoroPortfolioPositionDTO position)
    {
        assertEquals(3472201380L, position.getPositionId());
        assertEquals(15126196, position.getCid());
        assertEquals(OffsetDateTime.parse("2026-03-23T18:46:50.383Z"), position.getOpenDateTime());
        assertEquals(53518.7d, position.getOpenRate());
        assertEquals(36, position.getInstrumentId());
        assertTrue(position.isBuy());
        assertEquals(54856.67d, position.getTakeProfitRate());
        assertEquals(52180.68d, position.getStopLossRate());
        assertEquals(0, position.getMirrorId());
        assertEquals(0, position.getParentPositionID());
        assertEquals(99.99d, position.getAmount());
        assertEquals(20, position.getLeverage());
        assertEquals(337994705, position.getOrderId());
        assertEquals(17, position.getOrderType());
        assertEquals(5.906727d, position.getUnits());
        assertEquals(0.0d, position.getTotalFees());
        assertEquals(99.99d, position.getInitialAmountInDollars());
        assertFalse(position.isTslEnabled());
        assertEquals(1, position.getStopLossVersion());
        assertFalse(position.isSettled());
        assertEquals(0, position.getRedeemStatusId());
        assertEquals(5.906727d, position.getInitialUnits());
        assertFalse(position.isPartiallyAltered());
        assertEquals(99.99d, position.getUnitsBaseValueDollars());
        assertFalse(position.isDiscounted());
        assertEquals(0, position.getOpenPositionActionType());
        assertEquals(0, position.getSettlementTypeId());
        assertFalse(position.isDetached());
        assertEquals(0.00632607d, position.getOpenConversionRate());
        assertEquals(0, position.getPnlVersion());
        assertEquals(0.0d, position.getTotalExternalFees());
        assertFalse(position.isNoTakeProfit());
        assertFalse(position.isNoStopLoss());
        assertEquals(5.906727d, position.getLotCount());
    }

    private static void assertNasdaq100Position(EtoroPortfolioPositionDTO position)
    {
        assertEquals(3472201322L, position.getPositionId());
        assertEquals(15126196, position.getCid());
        assertEquals(OffsetDateTime.parse("2026-03-23T18:46:36.617Z"), position.getOpenDateTime());
        assertEquals(24296.98d, position.getOpenRate());
        assertEquals(28, position.getInstrumentId());
        assertTrue(position.isBuy());
        assertEquals(24904.4d, position.getTakeProfitRate());
        assertEquals(23689.56d, position.getStopLossRate());
        assertEquals(0, position.getMirrorId());
        assertEquals(0, position.getParentPositionID());
        assertEquals(99.99d, position.getAmount());
        assertEquals(20, position.getLeverage());
        assertEquals(338018368, position.getOrderId());
        assertEquals(17, position.getOrderType());
        assertEquals(0.082306d, position.getUnits());
        assertEquals(0.0d, position.getTotalFees());
        assertEquals(99.99d, position.getInitialAmountInDollars());
        assertFalse(position.isTslEnabled());
        assertEquals(99.99d, position.getInitialAmountInDollars());
        assertFalse(position.isSettled());
        assertEquals(0, position.getRedeemStatusId());
        assertEquals(0.082306d, position.getInitialUnits());
        assertFalse(position.isPartiallyAltered());
        assertEquals(99.99d, position.getUnitsBaseValueDollars());
        assertFalse(position.isDiscounted());
        assertEquals(0, position.getOpenPositionActionType());
        assertEquals(0, position.getSettlementTypeId());
        assertFalse(position.isDetached());
        assertEquals(1.0d, position.getOpenConversionRate());
        assertEquals(0, position.getPnlVersion());
        assertEquals(0.0d, position.getTotalExternalFees());
        assertFalse(position.isNoTakeProfit());
        assertFalse(position.isNoStopLoss());
        assertEquals(0.082306d, position.getLotCount());
    }

    private static void assertGoldPosition(EtoroPortfolioPositionDTO position)
    {
        assertEquals(3472107412L, position.getPositionId());
        assertEquals(15126196, position.getCid());
        assertEquals(3472107412L, position.getPositionId());
        assertEquals(OffsetDateTime.parse("2026-03-23T16:03:58.787Z"), position.getOpenDateTime());
        assertEquals(4364.61d, position.getOpenRate());
        assertEquals(18, position.getInstrumentId());
        assertTrue(position.isBuy());
        assertEquals(4473.73d, position.getTakeProfitRate());
        assertEquals(4255.49d, position.getStopLossRate());
        assertEquals(0, position.getMirrorId());
        assertEquals(0, position.getParentPositionID());
        assertEquals(99.99d, position.getAmount());
        assertEquals(20, position.getLeverage());
        assertEquals(337970127, position.getOrderId());
        assertEquals(17, position.getOrderType());
        assertEquals(0.458197d, position.getUnits());
        assertEquals(0.0d, position.getTotalFees());
        assertEquals(99.99d, position.getInitialAmountInDollars());
        assertFalse(position.isTslEnabled());
        assertEquals(99.99d, position.getInitialAmountInDollars());
        assertFalse(position.isSettled());
        assertEquals(0, position.getRedeemStatusId());
        assertEquals(0.458197d, position.getInitialUnits());
        assertFalse(position.isPartiallyAltered());
        assertEquals(99.99d, position.getUnitsBaseValueDollars());
        assertFalse(position.isDiscounted());
        assertEquals(0, position.getOpenPositionActionType());
        assertEquals(0, position.getSettlementTypeId());
        assertFalse(position.isDetached());
        assertEquals(1.0d, position.getOpenConversionRate());
        assertEquals(0, position.getPnlVersion());
        assertEquals(0.0d, position.getTotalExternalFees());
        assertFalse(position.isNoTakeProfit());
        assertFalse(position.isNoStopLoss());
        assertEquals(0.458197d, position.getLotCount());
    }
}
