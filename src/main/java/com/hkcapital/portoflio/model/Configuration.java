package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "configuration")
@NoArgsConstructor
@AllArgsConstructor
@Data
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
}
