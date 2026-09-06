package com.hkcapital.portflio.broker.etoro.instrument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@AllArgsConstructor
public class EtoroInstrument
{
    private Integer internalIndustryId;
    private Integer internalAssetClassId;
    private String internalInstrumentDisplayName;
    private Boolean isInternalInstrument;
    private String internalSymbolFull;
    private Boolean isHiddenFromClient;
    private Integer internalInstrumentId;
    private Integer internalCryptoTypeId;
    private Integer internalExchangeId;
    private String internalExchangeName;
    private String internalStockIndustryName;
    private String internalAssetClassName;

    private String logo35x35;
    private String logo50x50;
    private String logo150x150;

    private Double dailyPriceChange;
    private Double absDailyPriceChange;
    private Double weeklyPriceChange;
    private Double monthlyPriceChange;

    private Boolean isDelisted;
    private Boolean isCurrentlyTradable;
    private Boolean isExchangeOpen;
    private Double internalClosingPrice;
    private Boolean isActiveInPlatform;
    private Boolean isBuyEnabled;
}
