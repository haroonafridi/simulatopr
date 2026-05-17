package com.hkcapital.portflio.market.structure;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
@Getter
@ToString()
public class Distance
{
    private final Double referencePrice;
    private final Double currentPrice;
    private final Double points;
    private final Double percent;
    private final boolean isAbove;
    private final boolean isBelow;
    @Builder
    public Distance(
            final Double referencePrice,
            final Double currentPrice)
    {
        Objects.requireNonNull(referencePrice);
        Objects.requireNonNull(currentPrice);

        if (referencePrice == 0)
        {
            throw new IllegalArgumentException("referencePrice cannot be zero");
        }

        this.referencePrice = referencePrice;
        this.currentPrice = currentPrice;
        this.points = currentPrice - referencePrice;
        this.percent =
                ((currentPrice - referencePrice)
                        / referencePrice) * 100.0;
        isAbove = points > 0;
        isBelow = points < 0;
    }


    public double absPercent()
    {
        return Math.abs(percent);
    }

    public double absPoints()
    {
        return Math.abs(points);
    }
}