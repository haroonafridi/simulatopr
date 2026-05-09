package com.hkcapital.portflio.market.structure;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Range
{
    private Double low;
    private Double high;
}
