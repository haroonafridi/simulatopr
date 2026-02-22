package com.hkcapital.portoflio.order;

import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetails;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "etoro_orders")
public class EtoroOrder
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int instrumentID;
    private double amount;
    private boolean isBuy;

    private int leverage;
    private Double stopLossRate;
    private Double takeProfitRate;
    private Boolean isTslEnabled;
    private String status;

    private Double bid;

    private Double ask;

    private LocalDateTime dateTime;

    private String error;

    @Column(name = "order_id")
    private long orderID;

    private String tokenId;

    private String openDateTime;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

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

    public EtoroOrder fill(EtoroOrderDetails details)
    {
        instrumentID = details.getInstrumentID();
        amount = details.getAmount();
        isBuy = details.isBuy();
        leverage = details.getLeverage();
        stopLossRate = details.getStopLossRate();
        takeProfitRate = details.getTakeProfitRate();
        isTslEnabled = details.getTslEnabled();
        orderID = details.getOrderID();
        openDateTime = details.getOpenDateTime();
        this.dateTime = LocalDateTime.now();
        return this;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public String getTokenId()
    {
        return tokenId;
    }

    public void setTokenId(String tokenId)
    {
        this.tokenId = tokenId;
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

    public LocalDateTime getDateTime()
    {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime)
    {
        this.dateTime = dateTime;
    }
}
