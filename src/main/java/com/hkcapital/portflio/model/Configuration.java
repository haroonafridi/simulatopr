package com.hkcapital.portflio.model;

import com.hkcapital.portflio.service.configuration.dto.ConfigurationDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "configuration")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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

    public Configuration(Double percentAllocationAllowed, //
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

    public ConfigurationDTO buildDTO()
    {
        return ConfigurationDTO.builder()
                .percentAllocationAllowed(percentAllocationAllowed)
                .noOfInsutrments(noOfInsutrments)
                .noOfPositionsPerInstruments(noOfPositionsPerInstruments)
                .maxPercentAllowedPerInstrument(maxPercentAllowedPerInstrument)
                .lev(lev)
                .build();
    }
}
