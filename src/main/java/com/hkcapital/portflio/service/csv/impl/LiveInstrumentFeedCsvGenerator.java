package com.hkcapital.portflio.service.csv.impl;

import com.hkcapital.portflio.model.LiveInstrumentFeed;

import java.util.List;

public  class LiveInstrumentFeedCsvGenerator
{
    public static String generate(List<LiveInstrumentFeed> feeds)
    {
        StringBuilder sb = new StringBuilder();

        // Header
        sb.append("id,")
                .append("allow_buy,")
                .append("allow_sell,")
                .append("ask,")
                .append("ask_discounted,")
                .append("availability_reason,")
                .append("bid,")
                .append("bid_discounted,")
                .append("conversion_rate_ask,")
                .append("conversion_rate_bid,")
                .append("creation_date,")
                .append("delayed_ask,")
                .append("delayed_bid,")
                .append("feed_date,")
                .append("instrument_id,")
                .append("is_exchange_open,")
                .append("is_instrument_active,")
                .append("is_market_open,")
                .append("is_official_closing_price,")
                .append("last_execution,")
                .append("max_position_units,")
                .append("new_unit_margin,")
                .append("official_closing_price,")
                .append("price_rate_id,")
                .append("unit_margin_ask,")
                .append("unit_margin_ask_discounted,")
                .append("unit_margin_bid,")
                .append("unit_margin_bid_discounted")
                .append("\n");

        for (LiveInstrumentFeed feed : feeds)
        {
            sb.append(value(feed.getId())).append(",")
                    .append(value(feed.getAllowBuy())).append(",")
                    .append(value(feed.getAllowSell())).append(",")
                    .append(value(feed.getAsk())).append(",")
                    .append(value(feed.getAskDiscounted())).append(",")
                    .append(value(feed.getAvailabilityReason())).append(",")
                    .append(value(feed.getBid())).append(",")
                    .append(value(feed.getBidDiscounted())).append(",")
                    .append(value(feed.getConversionRateAsk())).append(",")
                    .append(value(feed.getConversionRateBid())).append(",")
                    .append(value(feed.getCreationDate())).append(",")
                    .append(value(feed.getDelayedAsk())).append(",")
                    .append(value(feed.getDelayedBid())).append(",")
                    .append(value(feed.getFeedDate())).append(",")
                    .append(value(feed.getInstrumentId())).append(",")
                    .append(value(feed.getIsExchangeOpen())).append(",")
                    .append(value(feed.getIsInstrumentActive())).append(",")
                    .append(value(feed.getIsMarketOpen())).append(",")
                    .append(value(feed.getIsOfficialClosingPrice())).append(",")
                    .append(value(feed.getLastExecution())).append(",")
                    .append(value(feed.getMaxPositionUnits())).append(",")
                    .append(value(feed.getNewUnitMargin())).append(",")
                    .append(value(feed.getOfficialClosingPrice())).append(",")
                    .append(value(feed.getPriceRateId())).append(",")
                    .append(value(feed.getUnitMarginAsk())).append(",")
                    .append(value(feed.getUnitMarginAskDiscounted())).append(",")
                    .append(value(feed.getUnitMarginBid())).append(",")
                    .append(value(feed.getUnitMarginBidDiscounted()))
                    .append("\n");
        }

        return sb.toString();
    }

    private static String value(Object value)
    {
        return value == null ? "" : value.toString();
    }
}