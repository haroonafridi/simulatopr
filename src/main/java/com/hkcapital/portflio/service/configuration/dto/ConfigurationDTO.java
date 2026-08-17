package com.hkcapital.portflio.service.configuration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Builder
public class ConfigurationDTO
{
    private Double percentAllocationAllowed;
    private Integer noOfInsutrments;
    private Integer noOfPositionsPerInstruments;

    private Double maxPercentAllowedPerInstrument;
    private Integer lev;

    public ConfigurationDTO(Double percentAllocationAllowed, //
                            Integer noOfInstruments, //
                            Integer noOfPositionsPerInstruments, //
                            Double maxPercentAllowedPerInstrument,
                            Integer lev)
    {
        this.percentAllocationAllowed = percentAllocationAllowed;
        this.noOfInsutrments = noOfInstruments;
        this.noOfPositionsPerInstruments = noOfPositionsPerInstruments;
        this.maxPercentAllowedPerInstrument = maxPercentAllowedPerInstrument;
        this.lev = lev;
    }
}
