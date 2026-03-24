package com.hkcapital.portoflio.etoro.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}