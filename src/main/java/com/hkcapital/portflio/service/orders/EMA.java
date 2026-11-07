package com.hkcapital.portflio.service.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class EMA
{
    private Double ema9;
    private Double ema20;
    private Double ema50;
    private Double ema100;
}
