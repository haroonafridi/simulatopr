package com.hkcapital.portoflio.indicators;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class SimpleMovingAverage
{
    private  static List<Double> runningSma = new ArrayList<Double>();
    public static DoubleSummaryStatistics of(List<Double> dataPoint)
    {
        return dataPoint.stream().mapToDouble(aDouble -> aDouble.doubleValue()).summaryStatistics();
    }
    public static double avg(List<Double> dataPoint)
    {
        return of(dataPoint).getAverage();
    }

    double max(List<Double> dataPoint)
    {
        return of(dataPoint).getMax();
    }


    public static double min(List<Double> dataPoint)
    {
        return of(dataPoint).getMin();
    }


    public static double runningSma(double dp)
    {
       return 1d; //runningSma.add(dp);
    }
}
