
package com.hkcapital.portflio.market.structure;

import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.NavigableSet;

@Getter
@Slf4j
public class MarketStructureDTO
{
    private final PriceRange priceRange;
    private final LocalDate creationDate = LocalDate.now();
    private final LocalDate marketDate;
    private final int intervals;

    private NavigableSet<MarketPriceBand> upperBands;
    private NavigableSet<MarketPriceBand> lowerBands;

    private final MarketStructureDTO child;

    private final Integer timeFrame;
    private final TimeFramesUnit timeFrameUnit;

    @Builder
    public MarketStructureDTO(final PriceRange priceRange,
                              final int intervals,
                              final Integer timeFrame,
                              final TimeFramesUnit timeFrameUnit,
                              final NavigableSet<MarketPriceBand> upperBands,
                              final NavigableSet<MarketPriceBand> lowerBands,
                              final MarketStructureDTO child,
                              final LocalDate marketDate)
    {
        this.intervals = intervals;
        this.priceRange = priceRange;
        this.timeFrame = timeFrame;
        this.timeFrameUnit = timeFrameUnit;
        this.upperBands = upperBands;
        this.lowerBands = lowerBands;
        this.child = child;
        this.marketDate = marketDate;
    }

    public static MarketStructureDTO from(final MarketStructure marketStructure)
    {
        if (marketStructure == null)
        {
            return null;
        }

        return MarketStructureDTO.builder()
                .priceRange(marketStructure.getPriceRange())
                .intervals(marketStructure.getIntervals())
                .timeFrame(marketStructure.getTimeFrame())
                .timeFrameUnit(marketStructure.getTimeFrameUnit())
                .upperBands(marketStructure.getUpperBands())
                .lowerBands(marketStructure.getLowerBands())
                .marketDate(marketStructure.getMarketDate())
                .child(from(marketStructure.getChildMarketStructure()))
                .build();
    }
}
