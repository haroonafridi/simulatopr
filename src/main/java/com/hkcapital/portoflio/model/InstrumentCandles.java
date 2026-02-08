package com.hkcapital.portoflio.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InstrumentCandles {

    private int instrumentId;
    private List<Candle> candles;

    private double rangeOpen;
    private double rangeClose;
    private double rangeHigh;
    private double rangeLow;
    private double volume;

    public InstrumentCandles() {}

    public int getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(int instrumentId) {
        this.instrumentId = instrumentId;
    }

    public List<Candle> getCandles() {
        return candles;
    }

    public void setCandles(List<Candle> candles) {
        this.candles = candles;
    }

    public double getRangeOpen() {
        return rangeOpen;
    }

    public void setRangeOpen(double rangeOpen) {
        this.rangeOpen = rangeOpen;
    }

    public double getRangeClose() {
        return rangeClose;
    }

    public void setRangeClose(double rangeClose) {
        this.rangeClose = rangeClose;
    }

    public double getRangeHigh() {
        return rangeHigh;
    }

    public void setRangeHigh(double rangeHigh) {
        this.rangeHigh = rangeHigh;
    }

    public double getRangeLow() {
        return rangeLow;
    }

    public void setRangeLow(double rangeLow) {
        this.rangeLow = rangeLow;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
