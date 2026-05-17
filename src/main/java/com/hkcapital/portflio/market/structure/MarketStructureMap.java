package com.hkcapital.portflio.market.structure;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 *
 */
public class MarketStructureMap
{
    private static Comparator<MarketPriceBand> SORT_BY_LEAST_VISITED_HIGH_BAND = (band1, band2) ->
    {
        if(band2.getMarketVisitCount() != 0 )
        {
            return band1.getMarketVisitCount().compareTo(band2.getMarketVisitCount());
        }
        return -1;
    };

    private static Comparator<MarketPriceBand> SORT_BY_HIGHEST_UPPERBOUND = (band1, band2) ->
            band2.getUpperBound().compareTo(band1.getUpperBound());

    private NavigableSet<MarketPriceBand> bands;
    private double price;

    public MarketStructureMap(NavigableSet<MarketPriceBand> bands, //
                              double price)
    {
        this.bands = bands;
        this.price = price;
    }

    public Integer findMaxMarketVisitCount(BandType type)
    {
        return bands.stream().filter(e -> e.getBandType().equals(type))
                .mapToInt(MarketPriceBand::getMarketVisitCount)
                .max().getAsInt();
    }

    public Integer findMinMarketVisitCount(BandType type)
    {
        return bands.stream().filter(e -> e.getBandType().equals(type))
                .mapToInt(MarketPriceBand::getMarketVisitCount)
                .min().getAsInt();
    }

    public Optional<MarketPriceBand> findNthMostVisitedHighBand(int position)
    {
        return bands.stream()
                .filter(b -> b.getBandType().equals(BandType.HIGH))
                .sorted(
                        Comparator.comparingInt(MarketPriceBand::getMarketVisitCount)
                                .reversed()
                )
                .skip(position)
                .findFirst();
    }

    public Optional<MarketPriceBand> findNthLeastVisitedHighBand(int position)
    {
        return  bands.stream()
                .filter(b -> b.getBandType().equals(BandType.HIGH))
                .sorted(SORT_BY_LEAST_VISITED_HIGH_BAND)
                .skip(position)
                .findFirst();
    }

    public Optional<MarketPriceBand> findNthLeastVisitedLowBand(int position)
    {
        return bands.stream()
                .filter(b -> b.getBandType().equals(BandType.LOW))
                .sorted(
                        Comparator.comparingInt(MarketPriceBand::getMarketVisitCount)
                )
                .skip(position)
                .findFirst();
    }

    public Optional<MarketPriceBand> findNthMostVisitedLowBand(int position)
    {
        return bands.stream()
                .filter(b -> b.getBandType().equals(BandType.LOW))
                .sorted(
                        Comparator.comparingInt(MarketPriceBand::getMarketVisitCount)
                                .reversed()
                )
                .skip(position)
                .findFirst();
    }

    public Integer findMinMarketVisitCountHighBand()
    {
        return bands.stream().filter(e -> e.getBandType().equals(BandType.HIGH))
                .mapToInt(MarketPriceBand::getMarketVisitCount)
                .min().getAsInt();
    }

    public Integer findMaxMarketVisitCountLowBand()
    {
        return bands.stream().filter(e -> e.getBandType().equals(BandType.LOW))
                .mapToInt(MarketPriceBand::getMarketVisitCount)
                .max().getAsInt();
    }


    public NavigableSet<MarketPriceBand> findBandByPrice()
    {
        return new TreeSet<>(bands.stream()
                .filter(e -> price >= e.getLowerBound() && price <= e.getUpperBound())
                .collect(Collectors.toList()));
    }

    public NavigableSet<MarketPriceBand> findBandByVisitCount(BandType bandType, int visitCount)
    {
        return new TreeSet<>(bands.stream()
                .filter(e -> e.getBandType().equals(bandType)
                        && e.getMarketVisitCount().compareTo(visitCount) == 0)
                .collect(Collectors.toList()));
    }

    Optional<MarketPriceBand> findMaxUpperBoundOf(BandType bandType)
    {
        return bands.stream().filter(e -> e.getBandType().equals(bandType)) //
                .sorted(SORT_BY_HIGHEST_UPPERBOUND).findFirst();
    }


}
