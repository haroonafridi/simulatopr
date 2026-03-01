package com.hkcapital.portoflio.etoro.dto.order;

import com.hkcapital.portoflio.etoro.CalcUtils;
import com.hkcapital.portoflio.etoro.JSON;
import com.hkcapital.portoflio.etoro.master.Instruments;

public class EtoroMarketOrderDto implements JSON
{
    private Integer instrumentId;
    private Boolean isBuy;
    private Integer leverage;
    private Double amount;
    private Double stopLossRate;
    private Double takeProfitRate;
    private Boolean isTslEnabled;

    private Boolean isNoStopLoss;

    private Boolean isNoTakeProfit;

    private String orderType;

    private Double bid;

    private Double ask;

    private Double maxAllowedSlippage;
    private Double etoroSlippage;



    public EtoroMarketOrderDto(Integer instrumentId, Boolean isBuy, Integer leverage, //
                               Double amount, Double stopLossRate, //
                               Double takeProfitRate, Boolean isTslEnabled, //
                               Boolean isNoStopLoss, //
                               Boolean isNoTakeProfit,
                               String orderType,
                               Double bid,
                               Double ask,
                               Double maxAllowedSlippage,
                               Double etoroSlippage)
    {
        this.instrumentId = instrumentId;
        this.isBuy = isBuy;
        this.leverage = leverage;
        this.amount = amount;
        this.stopLossRate = stopLossRate;
        this.takeProfitRate = takeProfitRate;
        this.isTslEnabled = isTslEnabled;
        this.isNoStopLoss = isNoStopLoss;
        this.isNoTakeProfit = isNoTakeProfit;
        this.orderType = orderType;
        this.bid = bid;
        this.ask = ask;
        this.maxAllowedSlippage = maxAllowedSlippage;
        this.etoroSlippage =etoroSlippage;
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

    public Double getAmount()
    {
        return amount;
    }

    public void setAmount(Double amount)
    {
        this.amount = amount;
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


    public void setOrderType(String orderType)
    {
        this.orderType = orderType;
    }

    public Double getBid()
    {
        return bid;
    }

    public void setBid(Double bid)
    {
        this.bid = bid;
    }

    public Double getAsk()
    {
        return ask;
    }

    public void setAsk(Double ask)
    {
        this.ask = ask;
    }

    public Double getMaxAllowedSlippage()
    {
        return maxAllowedSlippage;
    }

    public void setMaxAllowedSlippage(Double maxAllowedSlippage)
    {
        this.maxAllowedSlippage = maxAllowedSlippage;
    }

    public Double getEtoroSlippage()
    {
        return etoroSlippage;
    }

    public void setEtoroSlippage(Double etoroSlippage)
    {
        this.etoroSlippage = etoroSlippage;
    }

    @Override
    public String toString()
    {
        return "EtoroMarketOrderDto{" +
                "instrumentId=" + instrumentId +
                ", isBuy=" + isBuy +
                ", leverage=" + leverage +
                ", amount=" + amount +
                ", stopLossRate=" + stopLossRate +
                ", takeProfitRate=" + takeProfitRate +
                ", isTslEnabled=" + isTslEnabled +
                ", isNoStopLoss=" + isNoStopLoss +
                ", isNoTakeProfit=" + isNoTakeProfit +
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
                "\"StopLossRate\": " + stopLossRate + ",\n" +
                "\"TakeProfitRate\": " + takeProfitRate+ ",\n" +//CalcUtils.calculateTargetPrice(24950, 20, 50, 2) + ",\n" +
                "\"IsTslEnabled\": " + isTslEnabled + ",\n" +
                "\"IsNoStopLoss\": " + isNoStopLoss + ",\n" +
                "\"IsNoTakeProfit\": " + isNoTakeProfit + "\n" +
                "\n}";
    }

    public static EtoroMarketOrderDto createDummyOrderBtc()
    {
        return new EtoroMarketOrderDto(Instruments.BTC.getInstrumentId(),
                true, //
                1, //
                50d, //
                null, //
                null, //
                null, //
                null, //
                null,
                "AUTO",
                67000d,
                67010d,
                10d,
                5d);
    }

    public static EtoroMarketOrderDto createDummyOrderNasdaq100()
    {
        return new EtoroMarketOrderDto(Instruments.NASDAQ1100.getInstrumentId(),
                true, //
                20, //
                50d, //
                null, //
                null, //
                null, //
                null, //
                null,
                "AUTO",
                25000d,
                24700d,
                10d,
                5d);
    }


    public String getOrderType()
    {
        return orderType;
    }

    public static EtoroMarketOrderDto createDummyOrderGold()
    {
        return new EtoroMarketOrderDto(Instruments.GOLD.getInstrumentId(),
                true, //
                20, //
                50d, //
                5079d, //
                5090d, //
                true, //
                null, //
                null,
                "AUTO",
                5400d,
                5390d,
                10d,
                5d);
    }





}
