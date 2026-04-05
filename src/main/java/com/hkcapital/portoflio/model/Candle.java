package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "candle")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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


}
