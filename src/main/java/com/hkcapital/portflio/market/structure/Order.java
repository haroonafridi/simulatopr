package com.hkcapital.portflio.market.structure;

import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Order
{
    private Double openPrice;
    private OrderType orderType;
    private int leverage;
    private Double tp;
    private Double sl;
    @Setter
    private String status;
    @Setter
    private String info;
    LocalDateTime time;
    @Getter
    @Setter
    private boolean brokerSent;
}
