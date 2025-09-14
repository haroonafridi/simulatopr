package com.hkcapital.portoflio.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trading_sessions")
public class TradingTimeFrames
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "value")
    private Integer value;

    @Column(name = "unit")
    private String unit;

    public TradingTimeFrames()
    {

    }

    public TradingTimeFrames(Integer value, String unit)
    {
        this.value = value;
        this.unit = unit;
    }
}
