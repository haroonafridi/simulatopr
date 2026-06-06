package com.hkcapital.portflio.ui.panels.etoro.configuartion.tablemodels;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EtoroPortfolioOrderDto
{
    private long orderId;
    private Integer instrumentId;
    private Double amount;
    private Boolean isBuy;
    private Integer lev;
    String status;

    private String info;

}
