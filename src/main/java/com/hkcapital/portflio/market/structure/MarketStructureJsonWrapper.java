package com.hkcapital.portflio.market.structure;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonIgnoreProperties({"config"})
public class MarketStructureJsonWrapper
{
    private String uuid;
    private MarketStructure marketStructure;
}
