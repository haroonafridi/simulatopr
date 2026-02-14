package com.hkcapital.portoflio.etoro.dto;


public class CandleDto
{

    private int instrumentID;

    private String  fromDate;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    private String timeFrame;

    public CandleDto() {}

    public CandleDto(CandleDataInformationDto candleDataInformation)
    {
        this.instrumentID =candleDataInformation.getInstrumentID();
        this.fromDate = candleDataInformation.getFromDate();
        this.open = candleDataInformation.getOpen();
        this.high = candleDataInformation.getHigh();
        this.low = candleDataInformation.getLow();
        this.close = candleDataInformation.getClose();
        this.volume = candleDataInformation.getVolume();
        this.timeFrame = candleDataInformation.getInterval();
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int getInstrumentID()
    {
        return instrumentID;
    }

    public void setInstrumentID(int instrumentID)
    {
        this.instrumentID = instrumentID;
    }

    public String getTimeFrame()
    {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame)
    {
        this.timeFrame = timeFrame;
    }

    public String getFromDate()
    {
        return fromDate;
    }
}
