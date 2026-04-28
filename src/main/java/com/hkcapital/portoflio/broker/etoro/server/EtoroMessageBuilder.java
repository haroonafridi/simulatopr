package com.hkcapital.portoflio.broker.etoro.server;

import com.hkcapital.portoflio.service.api.etoro.websocket.LiveInstrumentRate;

public class EtoroMessageBuilder
{
    public static String buildMessage(final LiveInstrumentRate dto)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("\"messages\":[")
                .append("{")
                .append("\"topic\":\"instrument:").append(dto.getInstrumentId()).append("\",")
                .append("\"content\":\"{")
                .append("\\\"Ask\\\":\\\"").append(dto.getAsk()).append("\\\",")
                .append("\\\"Bid\\\":\\\"").append(dto.getBid()).append("\\\",")
                .append("\\\"LastExecution\\\":\\\"").append(dto.getLastExecution()).append("\\\",")
                .append("\\\"Date\\\":\\\"").append(dto.getDate()).append("\\\",")
                .append("\\\"NewUnitMargin\\\":\\\"").append(dto.getNewUnitMargin()).append("\\\",")
                .append("\\\"UnitMarginAsk\\\":\\\"").append(dto.getUnitMarginAsk()).append("\\\",")
                .append("\\\"UnitMarginBid\\\":\\\"").append(dto.getUnitMarginBid()).append("\\\",")
                .append("\\\"PriceRateID\\\":\\\"").append(dto.getPriceRateId()).append("\\\",")
                .append("\\\"BidDiscounted\\\":\\\"").append(dto.getBidDiscounted()).append("\\\",")
                .append("\\\"AskDiscounted\\\":\\\"").append(dto.getAskDiscounted()).append("\\\",")
                .append("\\\"UnitMarginBidDiscounted\\\":\\\"").append(dto.getUnitMarginBidDiscounted()).append("\\\",")
                .append("\\\"UnitMarginAskDiscounted\\\":\\\"").append(dto.getUnitMarginAskDiscounted()).append("\\\"")
                .append("}\",")
                .append("\"id\":\"").append(dto.getPriceRateId()).append("\",")
                .append("\"type\":\"Trading.Instrument.Rate\"")
                .append("}")
                .append("]")
                .append("}");
        return sb.toString();
    }
}
