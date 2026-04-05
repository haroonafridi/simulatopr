package com.hkcapital.portoflio.model;

import com.hkcapital.portoflio.broker.etoro.dto.candle.CandleDataInformationDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "candle")
@NoArgsConstructor
@AllArgsConstructor
@Data
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

}
