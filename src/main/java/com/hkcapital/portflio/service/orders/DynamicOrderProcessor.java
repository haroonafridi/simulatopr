package com.hkcapital.portflio.service.orders;

import com.hkcapital.portflio.market.indicators.CandleBuilder;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.market.structure.MarketPriceBand;
import com.hkcapital.portflio.market.structure.MarketStructure;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import com.hkcapital.portflio.service.candle.etoro.impl.SignalBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DynamicOrderProcessor implements OrderProcessor
{

    @Override
    public void process(LiveInstrumentRate liveInstrumentRate, SignalBuilder signalBuilder)
    {

        List<CandleBuilder> fiftenMinCb = signalBuilder.getCandleBuilder()
                .stream()
                .filter(e -> e.getInterval().intValue() == 1
                        && e.getTimeFrame().equals(TimeFramesUnit.MINUTE))
                .collect(Collectors.toList());

        CandleBuilder candleBuilder = fiftenMinCb.stream()
                .filter(cb -> cb.getInstrument()
                        .getInstrumentTicker()
                        .equals("XAUUSD"))
                .findFirst().get();


        MarketStructure str15MinsGld =
                getStructureByTimeFrame(candleBuilder.getMarketStructureManagerCache().getStructures().get("gold_4_hour"),
                        "MINUTE",15);


        MarketStructure str1HourGld =
                getStructureByTimeFrame(candleBuilder.getMarketStructureManagerCache().getStructures().get("gold_4_hour"),
                        "HOUR",1);

        MarketStructure str4HourGld =
                getStructureByTimeFrame(candleBuilder.getMarketStructureManagerCache().getStructures().get("gold_4_hour"),
                        "HOUR",4);

        MarketStructure str15MinsNsdq =
                getStructureByTimeFrame(candleBuilder.getMarketStructureManagerCache().getStructures().get("nasdaq_4_hour"),
                        "MINUTE",15);


        MarketStructure str1HourNsdq =
                getStructureByTimeFrame(candleBuilder.getMarketStructureManagerCache().getStructures().get("nasdaq_4_hour"),
                        "HOUR",1);

        MarketStructure str4HourNsdq =
                getStructureByTimeFrame(candleBuilder.getMarketStructureManagerCache().getStructures().get("nasdaq_4_hour"),
                        "HOUR",4);



        Map<String, IndicatorProcessor> ip = candleBuilder.getIndicatorProcessors();


        IndicatorProcessor SMA_9_1_HOUR = ip.get("SMA_9_1_HOUR");
        IndicatorProcessor SMA_20_1_HOUR = ip.get("SMA_20_1_HOUR");
        IndicatorProcessor SMA_50_1_HOUR = ip.get("SMA_50_1_HOUR");
        IndicatorProcessor SMA_100_1_HOUR = ip.get("SMA_100_1_HOUR");


        IndicatorProcessor SMA_9_1_MINUTE = ip.get("SMA_9_1_MINUTE");
        IndicatorProcessor SMA_20_1_MINUTE = ip.get("SMA_20_1_MINUTE");
        IndicatorProcessor SMA_50_1_MINUTE = ip.get("SMA_50_1_MINUTE");
        IndicatorProcessor SMA_100_1_MINUTE = ip.get("SMA_100_1_MINUTE");



        if( liveInstrumentRate.getAsk().doubleValue() < SMA_9_1_MINUTE.getValue()  &&
                SMA_9_1_MINUTE.getValue() > SMA_20_1_MINUTE.getValue())
        {
           List<MarketPriceBand> str =  str15MinsGld.getUpperBands()
                    .stream()
                    .collect(Collectors.toList());

            str.sort(Comparator.comparing(MarketPriceBand::getMarketVisitCount));

           System.out.println("Bullish signal reported "+str.get(0).getUpperBound()+" / "+str.get(0).getLowerBound());
        }


        if( liveInstrumentRate.getAsk().doubleValue() < SMA_9_1_MINUTE.getValue()
                &&  SMA_9_1_MINUTE.getValue() < SMA_20_1_MINUTE.getValue())
        {
            List<MarketPriceBand> str =  str15MinsGld.getUpperBands()
                    .stream()
                    .collect(Collectors.toList());

            str.sort((o1, o2) -> o2.getMarketVisitCount().compareTo(o1.getMarketVisitCount()));

            System.out.println("Sell signal reported "+str.get(0).getUpperBound()+" / "+str.get(0).getLowerBound());
        }

        IndicatorProcessor EMA_9_1_MINUTE = ip.get("EMA_9_1_MINUTE");
        IndicatorProcessor EMA_20_1_MINUTE = ip.get("EMA_20_1_MINUTE");
        IndicatorProcessor EMA_50_1_MINUTE = ip.get("EMA_50_1_MINUTE");
        IndicatorProcessor EMA_100_1_MINUTE = ip.get("EMA_100_1_MINUTE");

        IndicatorProcessor RSI_9_1_MINUTE = ip.get("RSI_9_1_MINUTE");
        IndicatorProcessor RSI_20_1_MINUTE = ip.get("RSI_20_1_MINUTE");
        IndicatorProcessor RSI_50_1_MINUTE = ip.get("RSI_50_1_MINUTE");
        IndicatorProcessor RSI_100_1_MINUTE = ip.get("RSI_100_1_MINUTE");

        IndicatorProcessor ATR_9_1_MINUTE = ip.get("ATR_9_1_MINUTE");
        IndicatorProcessor ATR_20_1_MINUTE = ip.get("ATR_20_1_MINUTE");
        IndicatorProcessor ATR_50_1_MINUTE = ip.get("ATR_50_1_MINUTE");
        IndicatorProcessor ATR_100_1_MINUTE = ip.get("ATR_100_1_MINUTE");

//        System.out.println("sma_9_1_min = " + SMA_9_1_MINUTE.getValue());
//        System.out.println("sma_20_1_min = " + SMA_20_1_MINUTE.getValue());
//        System.out.println("sma_50_1_min = " + SMA_50_1_MINUTE.getValue());
//        System.out.println("sma_100_1_min = " + SMA_100_1_MINUTE.getValue());
//
//
//        System.out.println("ema_9_1_min = " + EMA_9_1_MINUTE.getValue());
//        System.out.println("ema_20_1_min = " + EMA_20_1_MINUTE.getValue());
//        System.out.println("ema_50_1_min = " + EMA_50_1_MINUTE.getValue());
//        System.out.println("ema_100_1_min = " + EMA_100_1_MINUTE.getValue());
//
//        System.out.println("atr_9_1_min = " + ATR_9_1_MINUTE.getValue());
//        System.out.println("atr_20_1_min = " + ATR_20_1_MINUTE.getValue());
//        System.out.println("atr_50_1_min = " + ATR_50_1_MINUTE.getValue());
//        System.out.println("atr_100_1_min = " + ATR_100_1_MINUTE.getValue());
//
//        System.out.println("rsi_9_1_min = " + RSI_9_1_MINUTE.getValue());
//        System.out.println("rsi_20_1_min = " + RSI_20_1_MINUTE.getValue());
//        System.out.println("rsi_50_1_min = " + RSI_50_1_MINUTE.getValue());
//        System.out.println("rsi_100_1_min = " + RSI_100_1_MINUTE.getValue());

    }

    private MarketStructure getStructureByTimeFrame(MarketStructure structure, String timeFrame, Integer interval)
    {
        if (structure!=null && structure.getTimeFrameUnit().getUnit().equals(timeFrame) && structure.getTimeFrame().intValue() == interval)
        {
            return structure;
        }
        return getStructureByTimeFrame(structure.getChildMarketStructure(), timeFrame, interval);
    }
}

