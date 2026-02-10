package com.hkcapital.portoflio.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "candle")
public class Candle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "instrument_id", nullable = false)
    private int instrumentID;

    private String fromDate;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;

    private String timeFrame;

    public Candle() {}

    public Candle(CandleDataInformation candleDataInformation) {
        System.out.println("Instrument id => "+candleDataInformation.getInstrumentID());
        this.instrumentID =candleDataInformation.getInstrumentID();
        this.fromDate = candleDataInformation.getFromDate();
        this.open = candleDataInformation.getOpen();
        this.high = candleDataInformation.getHigh();
        this.low = candleDataInformation.getLow();
        this.close = candleDataInformation.getClose();
        this.volume = candleDataInformation.getVolume();
        this.timeFrame = candleDataInformation.getInterval();

    }


    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
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

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTimeFrame()
    {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame)
    {
        this.timeFrame = timeFrame;
    }

    @Override
    public String toString()
    {
        return "Candle{" +
                "id=" + id +
                ", instrumentID=" + instrumentID +
                ", fromDate='" + fromDate + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", timeFrame='" + timeFrame + '\'' +
                '}';
    }
}
