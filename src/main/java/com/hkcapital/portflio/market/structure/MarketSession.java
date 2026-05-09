package com.hkcapital.portflio.market.structure;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Builder
@Getter
@ToString
public class MarketSession
{
    private String name;
    private Instant start;
    private Instant end;
}
