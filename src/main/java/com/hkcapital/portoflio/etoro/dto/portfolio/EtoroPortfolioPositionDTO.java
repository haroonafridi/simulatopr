package com.hkcapital.portoflio.etoro.dto.portfolio;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class EtoroPortfolioPositionDTO
{
    @JsonProperty("positionID")
    private Long positionId;
    @JsonProperty("CID")
    private Integer cid;
    private OffsetDateTime openDateTime;
    @JsonProperty("openRate")
    private double openRate;
    @JsonProperty("instrumentID")
    private Integer instrumentId;
    @JsonProperty("isBuy")
    private boolean isBuy;
    @JsonProperty("takeProfitRate")
    private double takeProfitRate;
    @JsonProperty("stopLossRate")
    private double stopLossRate;
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("leverage")
    private Integer leverage;
    @JsonProperty("orderID")
    private Long orderId;
    @JsonProperty("orderType")
    private Integer orderType;
    @JsonProperty("units")
    private double units;
    @JsonProperty("totalFees")
    private double totalFees;
    @JsonProperty("initialAmountInDollars")
    private double initialAmountInDollars;
    @JsonProperty("isTslEnabled")
    private boolean isTslEnabled;
    @JsonProperty("stopLossVersion")
    private Integer stopLossVersion;
    @JsonProperty("isSettled")
    private boolean isSettled;
    @JsonProperty("redeemStatusID")
    private Integer redeemStatusId;
    @JsonProperty("initialUnits")
    private double initialUnits;
    @JsonProperty("isPartiallyAltered")
    private boolean isPartiallyAltered;
    @JsonProperty("unitsBaseValueDollars")
    private double unitsBaseValueDollars;
    @JsonProperty("isDiscounted")
    private boolean isDiscounted;
    @JsonProperty("openPositionActionType")
    private Integer openPositionActionType;
    @JsonProperty("settlementTypeID")
    private Integer settlementTypeId;
    @JsonProperty("isDetached")
    private boolean isDetached;
    @JsonProperty("openConversionRate")
    private double openConversionRate;
    @JsonProperty("pnlVersion")
    private Integer pnlVersion;
    @JsonProperty("totalExternalFees")
    private double totalExternalFees;
    @JsonProperty("totalExternalTaxes")
    private double totalExternalTaxes;
    @JsonProperty("isNoTakeProfit")
    private boolean isNoTakeProfit;
    @JsonProperty("isNoStopLoss")
    private boolean isNoStopLoss;
    @JsonProperty("lotCount")
    private double lotCount;

    public Long getPositionId()
    {
        return positionId;
    }

    public void setPositionId(Long positionId)
    {
        this.positionId = positionId;
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

    public double getOpenRate()
    {
        return openRate;
    }

    public void setOpenRate(double openRate)
    {
        this.openRate = openRate;
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

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Integer getOrderType()
    {
        return orderType;
    }

    public void setOrderType(Integer orderType)
    {
        this.orderType = orderType;
    }

    public double getUnits()
    {
        return units;
    }

    public void setUnits(double units)
    {
        this.units = units;
    }

    public double getTotalFees()
    {
        return totalFees;
    }

    public void setTotalFees(double totalFees)
    {
        this.totalFees = totalFees;
    }

    public double getInitialAmountInDollars()
    {
        return initialAmountInDollars;
    }

    public void setInitialAmountInDollars(double initialAmountInDollars)
    {
        this.initialAmountInDollars = initialAmountInDollars;
    }

    public boolean isTslEnabled()
    {
        return isTslEnabled;
    }

    public void setTslEnabled(boolean tslEnabled)
    {
        isTslEnabled = tslEnabled;
    }

    public Integer getStopLossVersion()
    {
        return stopLossVersion;
    }

    public void setStopLossVersion(Integer stopLossVersion)
    {
        this.stopLossVersion = stopLossVersion;
    }

    public boolean isSettled()
    {
        return isSettled;
    }

    public void setSettled(boolean settled)
    {
        isSettled = settled;
    }

    public Integer getRedeemStatusId()
    {
        return redeemStatusId;
    }

    public void setRedeemStatusId(Integer redeemStatusId)
    {
        this.redeemStatusId = redeemStatusId;
    }

    public double getInitialUnits()
    {
        return initialUnits;
    }

    public void setInitialUnits(double initialUnits)
    {
        this.initialUnits = initialUnits;
    }

    public boolean isPartiallyAltered()
    {
        return isPartiallyAltered;
    }

    public void setPartiallyAltered(boolean partiallyAltered)
    {
        isPartiallyAltered = partiallyAltered;
    }

    public double getUnitsBaseValueDollars()
    {
        return unitsBaseValueDollars;
    }

    public void setUnitsBaseValueDollars(double unitsBaseValueDollars)
    {
        this.unitsBaseValueDollars = unitsBaseValueDollars;
    }

    public boolean isDiscounted()
    {
        return isDiscounted;
    }

    public void setDiscounted(boolean discounted)
    {
        isDiscounted = discounted;
    }

    public Integer getOpenPositionActionType()
    {
        return openPositionActionType;
    }

    public void setOpenPositionActionType(Integer openPositionActionType)
    {
        this.openPositionActionType = openPositionActionType;
    }

    public Integer getSettlementTypeId()
    {
        return settlementTypeId;
    }

    public void setSettlementTypeId(Integer settlementTypeId)
    {
        this.settlementTypeId = settlementTypeId;
    }

    public boolean isDetached()
    {
        return isDetached;
    }

    public void setDetached(boolean detached)
    {
        isDetached = detached;
    }

    public double getOpenConversionRate()
    {
        return openConversionRate;
    }

    public void setOpenConversionRate(double openConversionRate)
    {
        this.openConversionRate = openConversionRate;
    }

    public Integer getPnlVersion()
    {
        return pnlVersion;
    }

    public void setPnlVersion(Integer pnlVersion)
    {
        this.pnlVersion = pnlVersion;
    }

    public double getTotalExternalFees()
    {
        return totalExternalFees;
    }

    public void setTotalExternalFees(double totalExternalFees)
    {
        this.totalExternalFees = totalExternalFees;
    }

    public double getTotalExternalTaxes()
    {
        return totalExternalTaxes;
    }

    public void setTotalExternalTaxes(double totalExternalTaxes)
    {
        this.totalExternalTaxes = totalExternalTaxes;
    }

    public boolean isNoTakeProfit()
    {
        return isNoTakeProfit;
    }

    public void setNoTakeProfit(boolean noTakeProfit)
    {
        isNoTakeProfit = noTakeProfit;
    }

    public boolean isNoStopLoss()
    {
        return isNoStopLoss;
    }

    public void setNoStopLoss(boolean noStopLoss)
    {
        isNoStopLoss = noStopLoss;
    }

    public double getLotCount()
    {
        return lotCount;
    }

    public void setLotCount(double lotCount)
    {
        this.lotCount = lotCount;
    }

    @Override
    public String toString()
    {
        return "EtoroPortfolioPositionDTO{" +
                "\n positionId=" + positionId +
                "\n , cid=" + cid +
                "\n, openDateTime=" + openDateTime +
                "\n , openRate=" + openRate +
                "\n , instrumentId=" + instrumentId +
                "\n , isBuy=" + isBuy +
                "\n , takeProfitRate=" + takeProfitRate +
                "\n , stopLossRate=" + stopLossRate +
                "\n , amount=" + amount +
                "\n , leverage=" + leverage +
                "\n , orderId=" + orderId +
                "\n, orderType=" + orderType +
                "\n, units=" + units +
                "\n , totalFees=" + totalFees +
                "\n , initialAmountInDollars=" + initialAmountInDollars +
                "\n , isTslEnabled=" + isTslEnabled +
                "\n , stopLossVersion=" + stopLossVersion +
                "\n , isSettled=" + isSettled +
                "\n , redeemStatusId=" + redeemStatusId +
                "\n , initialUnits=" + initialUnits +
                "\n , isPartiallyAltered=" + isPartiallyAltered +
                "\n , unitsBaseValueDollars=" + unitsBaseValueDollars +
                "\n , isDiscounted=" + isDiscounted +
                "\n , openPositionActionType=" + openPositionActionType +
                "\n , settlementTypeId=" + settlementTypeId +
                "\n , isDetached=" + isDetached +
                "\n , openConversionRate=" + openConversionRate +
                "\n , pnlVersion=" + pnlVersion +
                "\n , totalExternalFees=" + totalExternalFees +
                "\n , totalExternalTaxes=" + totalExternalTaxes +
                "\n , isNoTakeProfit=" + isNoTakeProfit +
                "\n , isNoStopLoss=" + isNoStopLoss +
                "\n , lotCount=" + lotCount +
                '}';
    }
}
