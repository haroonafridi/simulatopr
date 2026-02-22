package com.hkcapital.portoflio.etoro.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EtoroOrderDetails {
    @JsonProperty("instrumentID")
    private int instrumentID;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("isBuy")
    private boolean isBuy;

    @JsonProperty("leverage")
    private int leverage;

    @JsonProperty("stopLossRate")
    private Double stopLossRate; // Use Double (wrapper) to handle nulls

    @JsonProperty("takeProfitRate")
    private Double takeProfitRate;

    @JsonProperty("isTslEnabled")
    private Boolean isTslEnabled;

    @JsonProperty("orderID")
    private long orderID;

    @JsonProperty("openDateTime")
    private String openDateTime; // Can be ZonedDateTime with proper modules

    public int getInstrumentID()
    {
        return instrumentID;
    }

    public void setInstrumentID(int instrumentID)
    {
        this.instrumentID = instrumentID;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public boolean isBuy()
    {
        return isBuy;
    }

    public void setBuy(boolean buy)
    {
        isBuy = buy;
    }

    public int getLeverage()
    {
        return leverage;
    }

    public void setLeverage(int leverage)
    {
        this.leverage = leverage;
    }

    public Double getStopLossRate()
    {
        return stopLossRate;
    }

    public void setStopLossRate(Double stopLossRate)
    {
        this.stopLossRate = stopLossRate;
    }

    public Double getTakeProfitRate()
    {
        return takeProfitRate;
    }

    public void setTakeProfitRate(Double takeProfitRate)
    {
        this.takeProfitRate = takeProfitRate;
    }

    public Boolean getTslEnabled()
    {
        return isTslEnabled;
    }

    public void setTslEnabled(Boolean tslEnabled)
    {
        isTslEnabled = tslEnabled;
    }

    public long getOrderID()
    {
        return orderID;
    }

    public void setOrderID(long orderID)
    {
        this.orderID = orderID;
    }

    public String getOpenDateTime()
    {
        return openDateTime;
    }

    public void setOpenDateTime(String openDateTime)
    {
        this.openDateTime = openDateTime;
    }
}