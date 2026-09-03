package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.Instrument;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;


@Getter
@ToString
@EqualsAndHashCode(of = {"bandType", "bandKey", "lowerBound", "upperBound", "marketVisitCount"})
@AllArgsConstructor
@NoArgsConstructor
public class MarketPriceBand implements Comparable<MarketPriceBand>
{
    private BandType bandType;
    private BandKey bandKey;
    private Double lowerBound;
    private Double upperBound;
    private Integer marketVisitCount;
    private Instant initialVisitedTime;
    private Instant lastVisitedTime;
    private Instrument instrument;
    private long timeDifference;

    private Integer timeFrame;

    private TimeFramesUnit timeFrameUnit;

    @Builder
    public MarketPriceBand(BandType bandType, BandKey bandKey,
                           Double lowerBound, Double upperBound,
                           Integer marketVisitCount,
                           Instant initialVisitedTime,
                           Instant lastVisitedTime,
                           Integer timeFrame,
                           TimeFramesUnit timeFrameUnit,
                           Instrument instrument)
    {
        this.bandType = bandType;
        this.bandKey = bandKey;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.marketVisitCount = marketVisitCount;
        this.initialVisitedTime = initialVisitedTime;
        this.lastVisitedTime = lastVisitedTime;
        this.timeFrame = timeFrame;
        this.timeFrameUnit = timeFrameUnit;
        this.instrument = instrument;
    }

    @Override
    public int compareTo(MarketPriceBand other)
    {
        int typeCompare = this.bandType.compareTo(other.bandType);

        if (typeCompare != 0)
        {
            return typeCompare;
        }

        if (this.lowerBound != null)
        {
            return Double.compare(this.lowerBound, other.lowerBound);
        }
        return -1;
    }

    public void updateMarketVisitCount(int marketVisitCount) //
    {
        this.marketVisitCount = marketVisitCount;
    }

    public void updateTime(Instant updatedVisitTime) //
    {
        this.lastVisitedTime = updatedVisitTime;
    }

    public long getTimeDifference() //
    {
        if (lastVisitedTime == null)
        {
            return DateTimeUtil.minus(initialVisitedTime, initialVisitedTime, timeFrameUnit);
        }
        return DateTimeUtil.minus(lastVisitedTime, initialVisitedTime, timeFrameUnit);
    }
}
