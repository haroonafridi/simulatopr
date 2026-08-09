package com.hkcapital.portflio.service.srmatrix.impl;

import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.model.SRMatrixTolerance;

import java.util.Optional;

public class SRMatrixUtil
{
    public static void validate(SRMatrix srMatrix)
    {

    }

    public static Optional<SRMatrix> prepare(final SRMatrixTolerance srMatrixTolerance,
                                             final Double support,
                                             final Double resistance,
                                             final boolean isLong)
    {
        if (srMatrixTolerance == null
                || !srMatrixTolerance.getActive()
                || support == null
                || resistance == null)
        {
            return Optional.empty();
        }
        final SRMatrix srMatrix = new SRMatrix();

        double rSupportTolerance = Math.round(support + (support * srMatrixTolerance.getR_s_tolerance_percent() / 100));
        double lSupportTolerance = Math.round(support - (support * srMatrixTolerance.getL_s_tolerance_percent() / 100));

        double rResistanceTolerance = Math.round(resistance + (resistance * srMatrixTolerance.getR_r_tolerance_percent() / 100));
        double lResistanceTolerance = Math.round(resistance - (resistance * srMatrixTolerance.getL_r_tolerance_percent() / 100));
        double takeProfitP = 0;
        double stopLossP = 0;

        if (isLong)
        {
            takeProfitP = Math.round(support + (support * srMatrixTolerance.getTakeProfitPercent() / 100));
            stopLossP = Math.round(support - (support * srMatrixTolerance.getStopLossPercent() / 100));
        } else
        {
            takeProfitP = Math.round(resistance - (resistance * srMatrixTolerance.getTakeProfitPercent() / 100));
            stopLossP = Math.round(resistance + (support * srMatrixTolerance.getStopLossPercent() / 100));
        }

        srMatrix.setSupport(support);
        srMatrix.setResistance(resistance);
        srMatrix.setL_r_tolerance(lResistanceTolerance);
        srMatrix.setR_r_tolerance(rResistanceTolerance);
        srMatrix.setL_s_tolerance(lSupportTolerance);
        srMatrix.setR_s_tolerance(rSupportTolerance);
        srMatrix.setTakeProfit(takeProfitP);
        srMatrix.setStopLoss(stopLossP);
        return Optional.of(srMatrix);
    }
}
