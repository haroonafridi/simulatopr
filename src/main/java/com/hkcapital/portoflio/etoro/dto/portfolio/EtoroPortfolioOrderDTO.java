package com.hkcapital.portoflio.etoro.dto.portfolio;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
public class EtoroPortfolioOrderDTO {
    @JsonProperty("orderID")
    private Long orderId;
    @JsonProperty("CID")
    private Integer cid;
    private OffsetDateTime openDateTime;
    @JsonProperty("instrumentID")
    private Integer instrumentId;
    private boolean isBuy;
    private double takeProfitRate;
    private double stopLossRate;
    private double rate;
    private double amount;
    private Integer leverage;
    private double units;
    private boolean isTslEnabled;
    private Integer executionType;
    private boolean isDiscounted;

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Integer getCid()
    {
        return cid;
    }

    public void setCid(Integer cid)
    {
        this.cid = cid;
    }

    public OffsetDateTime getOpenDateTime()
    {
        return openDateTime;
    }

    public void setOpenDateTime(OffsetDateTime openDateTime)
    {
        this.openDateTime = openDateTime;
    }

    public Integer getInstrumentId()
    {
        return instrumentId;
    }

    public void setInstrumentId(Integer instrumentId)
    {
        this.instrumentId = instrumentId;
    }

    public boolean isBuy()
    {
        return isBuy;
    }

    public void setBuy(boolean buy)
    {
        isBuy = buy;
    }

    public double getTakeProfitRate()
    {
        return takeProfitRate;
    }

    public void setTakeProfitRate(double takeProfitRate)
    {
        this.takeProfitRate = takeProfitRate;
    }

    public double getStopLossRate()
    {
        return stopLossRate;
    }

    public void setStopLossRate(double stopLossRate)
    {
        this.stopLossRate = stopLossRate;
    }

    public double getRate()
    {
        return rate;
    }

    public void setRate(double rate)
    {
        this.rate = rate;
    }

    public double getAmount()
    {
        return amount;
    }

    public void setAmount(double amount)
    {
        this.amount = amount;
    }

    public Integer getLeverage()
    {
        return leverage;
    }

    public void setLeverage(Integer leverage)
    {
        this.leverage = leverage;
    }

    public double getUnits()
    {
        return units;
    }

    public void setUnits(double units)
    {
        this.units = units;
    }

    public boolean isTslEnabled()
    {
        return isTslEnabled;
    }

    public void setTslEnabled(boolean tslEnabled)
    {
        isTslEnabled = tslEnabled;
    }

    public Integer getExecutionType()
    {
        return executionType;
    }

    public void setExecutionType(Integer executionType)
    {
        this.executionType = executionType;
    }

    public boolean isDiscounted()
    {
        return isDiscounted;
    }

    public void setDiscounted(boolean discounted)
    {
        isDiscounted = discounted;
    }
}