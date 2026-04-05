package com.hkcapital.portoflio.broker.etoro.dto.order;

import com.hkcapital.portoflio.broker.etoro.JSON;
import lombok.*;

/**
 * Data Transfer object responbile for wrapping etoro Market Order
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
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
