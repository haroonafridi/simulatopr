package com.hkcapital.portflio.service.srmatrix.impl;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.model.SRMatrixTolerance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class SRMatrixUtilTest
{
    @Test
    public void shouldCreateGoldLongSRMatrix5MinsTF()
    {
        double support = 4100;
        double resistance = 4120;

        SRMatrixTolerance sRMatrixTolerance = new SRMatrixTolerance();
        sRMatrixTolerance.setTimeFrameUnit(TimeFramesUnit.MINUTE.getUnit());
        sRMatrixTolerance.setTimeFrame(5);
        sRMatrixTolerance.setL_s_tolerance_percent(0.08);
        sRMatrixTolerance.setR_s_tolerance_percent(0.08);
        sRMatrixTolerance.setL_r_tolerance_percent(0.08);
        sRMatrixTolerance.setR_r_tolerance_percent(0.08);
        sRMatrixTolerance.setStopLossPercent(0.2);
        sRMatrixTolerance.setTakeProfitPercent(0.2);

        final Optional<SRMatrix> srMatrix = //
                SRMatrixUtil.prepare(sRMatrixTolerance, support, resistance, true);

        Assertions.assertNotNull(srMatrix, "SRMatrix should not be null");
        Assertions.assertFalse(srMatrix.isEmpty(), "SRMatrix should not be empty");

        srMatrix.ifPresent(e ->
        {
            Assertions.assertAll("SRMatrix validation", () -> //
            {
                Assertions.assertEquals(4100, e.getSupport());
                Assertions.assertEquals(4120, e.getResistance());
                Assertions.assertEquals(4097.0, e.getL_s_tolerance());
                Assertions.assertEquals(4103.0, e.getR_s_tolerance());
                Assertions.assertEquals(4117.0, e.getL_r_tolerance());
                Assertions.assertEquals(4123.0, e.getR_r_tolerance());
                Assertions.assertEquals(4108.0, e.getTakeProfit());
                Assertions.assertEquals(4092.0, e.getStopLoss());
            });
        });

    }


    @Test
    public void shouldCreateGoldShortSRMatrix5MinsTF()
    {
        double support = 4100;
        double resistance = 4120;

        SRMatrixTolerance sRMatrixTolerance = new SRMatrixTolerance();
        sRMatrixTolerance.setTimeFrameUnit(TimeFramesUnit.MINUTE.getUnit());
        sRMatrixTolerance.setTimeFrame(5);
        sRMatrixTolerance.setL_s_tolerance_percent(0.08);
        sRMatrixTolerance.setR_s_tolerance_percent(0.08);
        sRMatrixTolerance.setL_r_tolerance_percent(0.08);
        sRMatrixTolerance.setR_r_tolerance_percent(0.08);
        sRMatrixTolerance.setStopLossPercent(0.2);
        sRMatrixTolerance.setTakeProfitPercent(0.2);

        final Optional<SRMatrix> srMatrix = //
                SRMatrixUtil.prepare(sRMatrixTolerance, support, resistance, false);

        Assertions.assertNotNull(srMatrix, "SRMatrix should not be null");
        Assertions.assertFalse(srMatrix.isEmpty(), "SRMatrix should not be empty");

        srMatrix.ifPresent(e ->
        {
            Assertions.assertAll("SRMatrix validation", () -> //
            {
                Assertions.assertEquals(4100, e.getSupport());
                Assertions.assertEquals(4120, e.getResistance());
                Assertions.assertEquals(4097.0, e.getL_s_tolerance());
                Assertions.assertEquals(4103.0, e.getR_s_tolerance());
                Assertions.assertEquals(4117.0, e.getL_r_tolerance());
                Assertions.assertEquals(4123.0, e.getR_r_tolerance());
                Assertions.assertEquals(4112.0, e.getTakeProfit());
                Assertions.assertEquals(4128.0, e.getStopLoss());
            });
        });

    }
}