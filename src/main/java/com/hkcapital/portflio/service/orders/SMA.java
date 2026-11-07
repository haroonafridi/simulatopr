package com.hkcapital.portflio.service.orders;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class SMA
{
    private Double sma9;
    private Double sma20;
    private Double sma50;
    private Double sma100;
}
