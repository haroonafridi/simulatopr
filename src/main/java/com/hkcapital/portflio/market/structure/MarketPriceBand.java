package com.hkcapital.portflio.market.structure;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode(of = {"bandType", "bandKey", "lowerBound", "upperBound", "marketVisitCount"})
public class MarketPriceBand  implements Comparable<MarketPriceBand>
{
    private BandType bandType;
    private BandKey bandKey;
    private Double lowerBound;
    private Double upperBound;
    private Integer marketVisitCount;

    @Override
    public int compareTo(MarketPriceBand other)
    {
        return Double.compare(this.lowerBound, other.lowerBound);
    }

    public void updateMarketVisitCount(int marketVisitCount) //
    {
        this.marketVisitCount = marketVisitCount;
    }
}
