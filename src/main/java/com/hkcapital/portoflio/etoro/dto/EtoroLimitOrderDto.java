package com.hkcapital.portoflio.etoro.dto;

import com.hkcapital.portoflio.etoro.JSON;

public class EtoroLimitOrderDto implements JSON
{
    private Integer instrumentId;
    private Boolean isBuy;
    private Integer leverage;
    private Double amount;
    private Double amountInUnits;
    private Double stopLossRate;
    private Double takeProfitRate;
    private Double rate;
    private Boolean isTslEnabled;

    private Boolean isDiscounted;

    private Boolean isNoStopLoss;

    private Boolean isNoTakeProfit;
    private Integer cid;

    public EtoroLimitOrderDto(Integer instrumentId, Boolean isBuy, Integer leverage, //
                              Double amount, Double amountInUnits, Double stopLossRate, //
                              Double takeProfitRate, Double rate, Boolean isTslEnabled, //
                              Boolean isDiscounted, Boolean isNoStopLoss, //
                              Boolean isNoTakeProfit, Integer cid)
    {
        this.instrumentId = instrumentId;
        this.isBuy = isBuy;
        this.leverage = leverage;
        this.amount = amount;
        this.amountInUnits = amountInUnits;
        this.stopLossRate = stopLossRate;
        this.takeProfitRate = takeProfitRate;
        this.rate = rate;
        this.isTslEnabled = isTslEnabled;
        this.isDiscounted = isDiscounted;
        this.isNoStopLoss = isNoStopLoss;
        this.isNoTakeProfit = isNoTakeProfit;
        this.cid = cid;
    }

    public Integer getInstrumentId()
    {
        return instrumentId;
    }

    public void setInstrumentId(Integer instrumentId)
    {
        this.instrumentId = instrumentId;
    }

    public Boolean getBuy()
    {
        return isBuy;
    }

    public void setBuy(Boolean buy)
    {
        isBuy = buy;
    }

    public Integer getLeverage()
    {
        return leverage;
    }

    public void setLeverage(Integer leverage)
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

    public Double getRate()
    {
        return rate;
    }

    public void setRate(Double rate)
    {
        this.rate = rate;
    }

    public Boolean getTslEnabled()
    {
        return isTslEnabled;
    }

    public void setTslEnabled(Boolean tslEnabled)
    {
        isTslEnabled = tslEnabled;
    }

    public Integer getCid()
    {
        return cid;
    }

    public void setCid(Integer cid)
    {
        this.cid = cid;
    }


    public Double getAmount()
    {
        return amount;
    }

    public void setAmount(Double amount)
    {
        this.amount = amount;
    }

    public Double getAmountInUnits()
    {
        return amountInUnits;
    }

    public void setAmountInUnits(Double amountInUnits)
    {
        this.amountInUnits = amountInUnits;
    }

    public Boolean getDiscounted()
    {
        return isDiscounted;
    }

    public void setDiscounted(Boolean discounted)
    {
        isDiscounted = discounted;
    }

    public Boolean getNoStopLoss()
    {
        return isNoStopLoss;
    }

    public void setNoStopLoss(Boolean noStopLoss)
    {
        isNoStopLoss = noStopLoss;
    }

    public Boolean getNoTakeProfit()
    {
        return isNoTakeProfit;
    }

    public void setNoTakeProfit(Boolean noTakeProfit)
    {
        isNoTakeProfit = noTakeProfit;
    }

    @Override
    public String toString()
    {
        return "EtoroLimitOrderDto{" +
                "instrumentId=" + instrumentId +
                ", isBuy=" + isBuy +
                ", leverage=" + leverage +
                ", amount=" + amount +
                ", amountInUnits=" + amountInUnits +
                ", stopLossRate=" + stopLossRate +
                ", takeProfitRate=" + takeProfitRate +
                ", rate=" + rate +
                ", isTslEnabled=" + isTslEnabled +
                ", isDiscounted=" + isDiscounted +
                ", isNoStopLoss=" + isNoStopLoss +
                ", isNoTakeProfit=" + isNoTakeProfit +
                ", cid=" + cid +
                '}';
    }

    @Override
    public String toJson()
    {
        return "{\n" +
                "\"InstrumentID\": " + instrumentId + ",\n" +
                "\"IsBuy\": " + isBuy + ",\n" +
                "\"Leverage\": " + leverage + ",\n" +
                "\"Amount\": " + amount + ",\n" +
                "\"AmountInUnits\": " + amountInUnits + ",\n" +
                "\"StopLossRate\": " + stopLossRate + ",\n" +
                "\"TakeProfitRate\": " + takeProfitRate + ",\n" +
                "\"Rate\": " + rate + ",\n" +
                "\"IsTslEnabled\": " + isTslEnabled + ",\n" +
                "\"IsDiscounted\": " + isDiscounted + ",\n" +
                "\"IsNoStopLoss\": " + isNoStopLoss + ",\n" +
                "\"IsNoTakeProfit\": " + isNoTakeProfit + ",\n" +
                "\"CID\": " + cid + "\n" +
                "\n}";
    }


    public static EtoroLimitOrderDto createDummyOrder() //
    {
        return  new EtoroLimitOrderDto(219000, true,
                1, 65000d, 1d, 79000d,
                85000d, 1.2d, false, false,
                true, false, 1234);
    }
}

