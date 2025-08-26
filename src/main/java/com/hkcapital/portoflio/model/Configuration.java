package com.hkcapital.portoflio.model;

import jakarta.persistence.*;

@Entity
@Table(name = "configuration")
public class Configuration
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "percent_allocation_allowed")
    private Double percentAllocationAllowed;
    @Column(name = "no_of_instruments")
    private Integer noOfInsutrments;
    @Column(name = "no_of_positions_per_instrument")
    private Integer noOfPositionsPerInstruments;
    @Column(name = "max_percent_allowed_per_instrument")
    private Double maxPercentAllowedPerInstrument;
    private Integer lev;

    public Configuration( Double percentAllocationAllowed, //
                         Integer noOfInsutrments, //
                         Integer noOfPositionsPerInstruments, //
                         Double maxPercentAllowedPerInstrument,
                         Integer lev)
    {
        this.percentAllocationAllowed = percentAllocationAllowed;
        this.noOfInsutrments = noOfInsutrments;
        this.noOfPositionsPerInstruments = noOfPositionsPerInstruments;
        this.maxPercentAllowedPerInstrument = maxPercentAllowedPerInstrument;
        this.lev = lev;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Double getPercentAllocationAllowed()
    {
        return percentAllocationAllowed;
    }

    public void setPercentAllocationAllowed(Double percentAllocationAllowed)
    {
        this.percentAllocationAllowed = percentAllocationAllowed;
    }

    public Integer getNoOfInsutrments()
    {
        return noOfInsutrments;
    }

    public void setNoOfInsutrments(Integer noOfInsutrments)
    {
        this.noOfInsutrments = noOfInsutrments;
    }

    public Integer getNoOfPositionsPerInstruments()
    {
        return noOfPositionsPerInstruments;
    }

    public void setNoOfPositionsPerInstruments(Integer noOfPositionsPerInstruments)
    {
        this.noOfPositionsPerInstruments = noOfPositionsPerInstruments;
    }

    public Double getMaxPercentAllowedPerInstrument()
    {
        return maxPercentAllowedPerInstrument;
    }

    public void setMaxPercentAllowedPerInstrument(Double maxPercentAllowedPerInstrument)
    {
        this.maxPercentAllowedPerInstrument = maxPercentAllowedPerInstrument;
    }

    public Integer getLev()
    {
        return lev;
    }

    public void setLev(Integer lev)
    {
        this.lev = lev;
    }
}
