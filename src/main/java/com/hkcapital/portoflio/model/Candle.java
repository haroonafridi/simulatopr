package com.hkcapital.portoflio.model;

import com.hkcapital.portoflio.etoro.dto.CandleDataInformationDto;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "candle")
public class Candle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "instrument_id", nullable = false)
    private int instrumentID;

    private Instant  fromDate;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;

    private String timeFrame;


    private LocalDateTime creationDateTime;

    public Candle() {}

    public Candle(CandleDataInformationDto candleDataInformation)
    {
        this.instrumentID =candleDataInformation.getInstrumentID();
        this.fromDate = Instant.parse(candleDataInformation.getFromDate());
        this.open = candleDataInformation.getOpen();
        this.high = candleDataInformation.getHigh();
        this.low = candleDataInformation.getLow();
        this.close = candleDataInformation.getClose();
        this.volume = candleDataInformation.getVolume();
        this.timeFrame = candleDataInformation.getInterval();
        this.creationDateTime = LocalDateTime.now();
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
                ", fromDate=" + fromDate +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", timeFrame='" + timeFrame + '\'' +
                ", creationDateTime=" + creationDateTime +
                '}';
    }

    public Instant getFromDate()
    {
        return fromDate;
    }

    public void setFromDate(Instant fromDate)
    {
        this.fromDate = fromDate;
    }

    public LocalDateTime getCreationDateTime()
    {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime)
    {
        this.creationDateTime = creationDateTime;
    }
}
