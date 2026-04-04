package com.hkcapital.portoflio.etoro.websocket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LiveInstrumentRate
{
    @JsonProperty("InstrumentID") Integer instrumentId;
    @JsonProperty("Ask") Double ask;
    @JsonProperty("Bid") Double bid;
    @JsonProperty("PriceRateID") Long priceRateId;
    @JsonProperty("LastExecution") Double lastExecution;
    @JsonProperty("ConversionRateAsk") Double conversionRateAsk;
    @JsonProperty("ConversionRateBid") Double conversionRateBid;
    @JsonProperty("NewUnitMargin") Double newUnitMargin;
    @JsonProperty("UnitMarginAsk") Double unitMarginAsk;
    @JsonProperty("UnitMarginBid") Double unitMarginBid;
    @JsonProperty("BidDiscounted") Double bidDiscounted;
    @JsonProperty("AskDiscounted") Double askDiscounted;
    @JsonProperty("UnitMarginBidDiscounted") Double unitMarginBidDiscounted;
    @JsonProperty("UnitMarginAskDiscounted") Double unitMarginAskDiscounted;
    @JsonProperty("IsInstrumentActive") Boolean isInstrumentActive;
    @JsonProperty("OfficialClosingPrice") Double officialClosingPrice;
    @JsonProperty("IsMarketOpen") Boolean isMarketOpen;
    @JsonProperty("AllowBuy") Boolean allowBuy;
    @JsonProperty("AllowSell") Boolean allowSell;
    @JsonProperty("MaxPositionUnits") Integer maxPositionUnits;
    @JsonProperty("IsExchangeOpen") Boolean isExchangeOpen;
    @JsonProperty("DelayedAsk") Integer delayedAsk;
    @JsonProperty("DelayedBid") Integer delayedBid;
    @JsonProperty("AvailabilityReason") Integer availabilityReason;
    @JsonProperty("IsOfficialClosingPrice") Boolean isOfficialClosingPrice;
    @JsonProperty("Date") Instant date;
}