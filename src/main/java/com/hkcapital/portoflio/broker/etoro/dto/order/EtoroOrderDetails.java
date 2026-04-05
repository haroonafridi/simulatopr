package com.hkcapital.portoflio.broker.etoro.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private Double stopLossRate;

    @JsonProperty("takeProfitRate")
    private Double takeProfitRate;

    @JsonProperty("isTslEnabled")
    private Boolean isTslEnabled;

    @JsonProperty("orderID")
    private long orderID;

    @JsonProperty("openDateTime")
    private Instant openDateTime;
}