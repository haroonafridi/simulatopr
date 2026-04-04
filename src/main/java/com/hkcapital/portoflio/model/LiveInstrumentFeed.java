package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@ToString
@Entity
@Table(name = "live_instrument_feed")
@NoArgsConstructor
@AllArgsConstructor
public class LiveInstrumentFeed
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer instrumentId;
    Double ask;
    Double bid;
    Long priceRateId;
    Double lastExecution;
    Double conversionRateAsk;
    Double conversionRateBid;
    Double newUnitMargin;
    Double unitMarginAsk;
    Double unitMarginBid;
    Double bidDiscounted;
    Double askDiscounted;
    Double unitMarginBidDiscounted;
    Double unitMarginAskDiscounted;
    Boolean isInstrumentActive;
    Double officialClosingPrice;
    Boolean isMarketOpen;
    Boolean allowBuy;
    Boolean allowSell;
    Integer maxPositionUnits;
    Boolean isExchangeOpen;
    Integer delayedAsk;
    Integer delayedBid;
    Integer availabilityReason;
    Boolean isOfficialClosingPrice;
    Instant feedDate;
    Instant creationDate;

}