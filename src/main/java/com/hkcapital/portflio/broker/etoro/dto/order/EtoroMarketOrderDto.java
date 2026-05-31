package com.hkcapital.portflio.broker.etoro.dto.order;

import com.hkcapital.portflio.broker.etoro.JSON;
import com.hkcapital.portflio.values.timeframe.TimeFrame;
import lombok.*;

import java.io.Serializable;

/**
 * Data Transfer object responbile for wrapping etoro Market Order
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class EtoroMarketOrderDto implements JSON , Serializable
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
    private String orderInfo;

    private TimeFrame timeFrame;


    @Override
    public String toJson()
    {
        return "{\n" +
                "\"InstrumentID\": " + instrumentId + ",\n" +
                "\"IsBuy\": " + isBuy + ",\n" +
                "\"Leverage\": " + leverage + ",\n" +
                "\"Amount\": " + amount + ",\n" +
                "\"StopLossRate\": " + stopLossRate + ",\n" +
                "\"TakeProfitRate\": " + takeProfitRate + ",\n" +//CalcUtils.calculateTargetPrice(24950, 20, 50, 2) + ",\n" +
                "\"IsTslEnabled\": " + isTslEnabled + ",\n" +
                "\"IsNoStopLoss\": " + isNoStopLoss + ",\n" +
                "\"IsNoTakeProfit\": " + isNoTakeProfit + "\n" +
                "\n}";
    }

}
