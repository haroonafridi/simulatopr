package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "candle")
@NoArgsConstructor

public class Candle
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "instrument_id", nullable = false)
    private int instrumentID;

    private Instant fromDate;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;

    private String timeFrame;

    private LocalDateTime creationDateTime;

    @Builder
    public Candle(int instrumentID, Instant fromDate, double open, //
                  double high, double low, double close, //
                  double volume, String timeFrame, //
                  LocalDateTime creationDateTime)
    {
        this.instrumentID = instrumentID;
        this.fromDate = fromDate;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.timeFrame = timeFrame;
        this.creationDateTime = creationDateTime;
    }
}
