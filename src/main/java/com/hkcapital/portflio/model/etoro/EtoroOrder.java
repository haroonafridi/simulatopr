package com.hkcapital.portflio.model.etoro;

import com.hkcapital.portflio.broker.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portflio.values.timeframe.TimeFrame;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "etoro_orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private Instant dateTime;
    private String error;
    @Column(name = "order_id")
    private long orderID;
    private String tokenId;
    @Column(columnDefinition = "TIMESTAMP(6)")
    private Instant openDateTime;
    private String oderType;
    private Double maxAllowedSlippage;
    private Double etoroSlippage;
    @Column(name = "order_info")
    private String orderInfo;

    private Integer timeFrame;
    private String timeFrameUnit;

    public EtoroOrder fill(EtoroOrderDetails details, TimeFrame timeFrame)
    {
        instrumentID = details.getInstrumentID();
        amount = details.getAmount();
        isBuy = details.isBuy();
        leverage = details.getLeverage();
        stopLossRate = details.getStopLossRate();
        takeProfitRate = details.getTakeProfitRate();
        isTslEnabled = details.getIsTslEnabled();
        orderID = details.getOrderID();
        openDateTime = details.getOpenDateTime();
        this.dateTime = Instant.now();
        this.timeFrame = timeFrame.timeFrame();
        this.timeFrameUnit = timeFrame.timeFrameUnit();
        return this;
    }
}
