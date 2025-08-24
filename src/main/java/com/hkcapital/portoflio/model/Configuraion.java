package com.hkcapital.portoflio.model;

public class Configuraion
{
    private Integer id;
    private Double percentAllocationAllowed;
    private Integer noOfInsutrments;
    private Integer noOfPositionsPerInstruments;
    private Double maxPercentAllowedPerInstrument;
    private Integer lev;

    public Configuraion(Integer id, Double percentAllocationAllowed, //
                        Integer noOfInsutrments, //
                        Integer noOfPositionsPerInstruments, //
                        Double maxPercentAllowedPerInstrument,
                        Integer lev)
    {
        this.id = id;
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
