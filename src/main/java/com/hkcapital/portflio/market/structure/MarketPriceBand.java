package com.hkcapital.portflio.market.structure;

import lombok.*;

import java.time.LocalDateTime;


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
    private LocalDateTime initialVisitedTime;
    private LocalDateTime lastVisitedTime;
    private long timeDifference;
    @Builder
    public MarketPriceBand(BandType bandType, BandKey bandKey,
                           Double lowerBound, Double upperBound,
                           Integer marketVisitCount, LocalDateTime initialVisitedTime,
                           LocalDateTime lastVisitedTime)
    {
        this.bandType = bandType;
        this.bandKey = bandKey;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.marketVisitCount = marketVisitCount;
        this.initialVisitedTime = initialVisitedTime;
        this.lastVisitedTime = lastVisitedTime;
    }

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

    public void updateTime(LocalDateTime updatedVisitTime) //
    {
        this.lastVisitedTime = updatedVisitTime;
    }

    public long getTimeDifference() //
    {
        return lastVisitedTime.getSecond() - initialVisitedTime.getSecond();
    }
}
