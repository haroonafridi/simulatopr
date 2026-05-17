package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import lombok.*;

import java.time.Instant;

@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLogger
{
    private MarketPriceBand marketPriceBand;
    private Distance distance;
    private OrderType orderType;
    private Double price;
    private Double absPoint;
    private Double pTAbove;
    private Double pTBelow;
    private Integer orderCount;
    private Instant instant;
}
