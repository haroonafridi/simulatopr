package com.hkcapital.portoflio.model;

import jakarta.persistence.*;

@Entity
@Table(name = "market_conditions")
public class MarketConditions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "instrument_id", referencedColumnName = "id")
    private Instrument instrument;
    @Column(name="day_low")
    private Double dayLow;
    @Column(name="day_high")
    private Double dayHigh;
    @Column(name = "percent_move")
    private Double percentMove;

    public MarketConditions()
    {
    }

    public MarketConditions(Instrument instrument, Double dayLow, Double dayHigh, Double percentMove)
    {
        this.instrument = instrument;
        this.dayLow = dayLow;
        this.dayHigh = dayHigh;
        this.percentMove = percentMove;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Instrument getInstrument()
    {
        return instrument;
    }

    public void setInstrument(Instrument instrument)
    {
        this.instrument = instrument;
    }

    public Double getDayLow()
    {
        return dayLow;
    }

    public void setDayLow(Double dayLow)
    {
        this.dayLow = dayLow;
    }

    public Double getDayHigh()
    {
        return dayHigh;
    }

    public void setDayHigh(Double dayHigh)
    {
        this.dayHigh = dayHigh;
    }

    public Double getPercentMove()
    {
        return percentMove;
    }

    public void setPercentMove(Double percentMove)
    {
        this.percentMove = percentMove;
    }
}
