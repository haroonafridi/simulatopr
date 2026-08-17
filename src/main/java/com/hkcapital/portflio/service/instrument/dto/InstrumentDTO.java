package com.hkcapital.portflio.service.instrument.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InstrumentDTO
{
    private String name;
    private String url;
    private String instrumentTicker;
    private String instrumentDesc;
    private Integer etoroInstrumentId;
    private Double maxSlippage;
    private Boolean active;
}
