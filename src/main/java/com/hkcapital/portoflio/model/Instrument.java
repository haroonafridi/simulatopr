package com.hkcapital.portoflio.model;

import jakarta.persistence.*;

@Entity
@Table(name = "instruments")
public class Instrument
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "instrument_name")
    private String name;

    @Column(name = "etoro_instrument_id")
    private Integer etoroInstrumentId;

    @Column(name = "max_slippage")
    private Double maxSlippage;

    @Column(name = "active")
    private Boolean active;

    public Instrument () {

    }
    public Instrument(String name)
    {
        this.name = name;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public Integer getEtoroInstrumentId()
    {
        return etoroInstrumentId;
    }

    public void setEtoroInstrumentId(Integer etoroInstrumentId)
    {
        this.etoroInstrumentId = etoroInstrumentId;
    }

    public Double getMaxSlippage()
    {
        return maxSlippage;
    }

    public void setMaxSlippage(Double maxSlippage)
    {
        this.maxSlippage = maxSlippage;
    }

    public Boolean isActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }
}
