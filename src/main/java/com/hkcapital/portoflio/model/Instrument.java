package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instruments")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Instrument
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "instrument_name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "instrument_ticker")
    private String instrumentTicker;


    @Lob
    @Column(name = "instrument_desc" , columnDefinition = "TEXT")
    private String instrumentDesc;

    @Column(name = "etoro_instrument_id")
    private Integer etoroInstrumentId;

    @Column(name = "max_slippage")
    private Double maxSlippage;

    @Column(name = "active")
    private Boolean active;
}
