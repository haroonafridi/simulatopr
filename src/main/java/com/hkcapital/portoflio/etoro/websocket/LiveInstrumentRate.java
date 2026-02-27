package com.hkcapital.portoflio.etoro.websocket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LiveInstrumentRate
{
    @JsonProperty("Ask") String ask;
    @JsonProperty("Bid") String bid;
    @JsonProperty("PriceRateID") String priceRateId;
    @JsonProperty("Date") String date;

    public String getAsk()
    {
        return ask;
    }

    public void setAsk(String ask)
    {
        this.ask = ask;
    }

    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid)
    {
        this.bid = bid;
    }

    public String getPriceRateId()
    {
        return priceRateId;
    }

    public void setPriceRateId(String priceRateId)
    {
        this.priceRateId = priceRateId;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return "LiveInstrumentRate{" +
                "ask='" + ask + '\'' +
                ", bid='" + bid + '\'' +
                ", priceRateId='" + priceRateId + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}