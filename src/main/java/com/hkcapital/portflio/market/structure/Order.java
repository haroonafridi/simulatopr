package com.hkcapital.portflio.market.structure;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
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
}
