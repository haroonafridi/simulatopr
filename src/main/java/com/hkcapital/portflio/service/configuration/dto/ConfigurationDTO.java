package com.hkcapital.portflio.service.configuration.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ConfigurationDTO
{
    private Double percentAllocationAllowed;
    private Integer noOfInsutrments;
    private Integer noOfPositionsPerInstruments;
    private String code;
    private Double maxPercentAllowedPerInstrument;
    private Integer lev;

    @Builder
    public ConfigurationDTO(Double percentAllocationAllowed, //
                            Integer noOfInstruments, //
                            Integer noOfPositionsPerInstruments, //
                            Double maxPercentAllowedPerInstrument,
                            Integer lev,
                            String code)
    {
        this.percentAllocationAllowed = percentAllocationAllowed;
        this.noOfInsutrments = noOfInstruments;
        this.noOfPositionsPerInstruments = noOfPositionsPerInstruments;
        this.maxPercentAllowedPerInstrument = maxPercentAllowedPerInstrument;
        this.lev = lev;
        this.code = code;
    }
}
