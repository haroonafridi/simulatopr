package com.hkcapital.portoflio.model;

import jakarta.persistence.*;

@Entity
@Table(name = "positions")
public class Position
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "instrument_id", referencedColumnName = "id")
    private Instrument instrument;
    @Column(name = "percent_capital_deployed")
    private Double percentCapitalDeployed;

    public Position(Integer index, Instrument instrument, Double percentCapitalDeployed)
    {
        this.id = index;
        this.instrument = instrument;
        this.percentCapitalDeployed = percentCapitalDeployed;
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

    public Double getPercentCapitalDeployed()
    {
        return percentCapitalDeployed;
    }

    public void setPercentCapitalDeployed(Double percentCapitalDeployed)
    {
        this.percentCapitalDeployed = percentCapitalDeployed;
    }
}
