package com.hkcapital.portflio.market.structure;

import lombok.*;

import java.time.Instant;

@Builder
@Getter
@ToString
@EqualsAndHashCode(of = {"bandType", "bandKey", "lowerBound", "upperBound", "marketVisitCount"})
@AllArgsConstructor
@NoArgsConstructor
public class MarketPriceBand  implements Comparable<MarketPriceBand>
{
    private BandType bandType;
    private BandKey bandKey;
    private Double lowerBound;
    private Double upperBound;
    private Integer marketVisitCount;
    private Instant time;

    @Override
    public int compareTo(MarketPriceBand other)
    {
        int typeCompare = this.bandType.compareTo(other.bandType);

        if (typeCompare != 0)
        {
            return typeCompare;
        }

        if(this.lowerBound != null ) {
            return Double.compare(this.lowerBound, other.lowerBound);
        }
        return -1;
    }

    public void updateMarketVisitCount(int marketVisitCount) //
    {
        this.marketVisitCount = marketVisitCount;
    }

    public void updateTime(Instant time) //
    {
        this.time = time;
    }
}
