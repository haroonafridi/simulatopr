package com.hkcapital.portflio.market.structure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Builder
@JsonIgnoreProperties({"config"})
public class MarketStructureJsonWrapper
{
    private String uuid;
    private String ticker;
    private LocalDate marketDate;
    private LocalDate creationDate;
    private PreviousDayMarketRangeDTO previousDayRange;
    private MarketStructureDTO marketStructure;
}



