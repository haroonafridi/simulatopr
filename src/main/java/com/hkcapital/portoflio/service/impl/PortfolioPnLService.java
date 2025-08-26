package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.*;

import java.util.ArrayList;
import java.util.List;

public class PortfolioPnLService
{
    private final List<Instrument> instruments;
    private final Configuration configuraion;

    private final List<MarketConditions> marketConditions;

    private final List<Position> positionList;

    private final OpeningCapital openingCapital;

    private List<PositionPnL> positionPnLList = new ArrayList<>();

    public PortfolioPnLService(final List<Instrument> instruments, final Configuration configuraion, //
                               final List<MarketConditions> marketConditions,//
                               final List<Position> positionList,
                               final OpeningCapital openingCapital)
    {
        this.instruments = instruments;
        this.configuraion = configuraion;
        this.marketConditions = marketConditions;
        this.positionList = positionList;
        this.openingCapital = openingCapital;
    }

    public void addPosition(Position p)
    {
        positionList.add(p);
    }


    public void addMarkerCondition(MarketConditions m)
    {
        marketConditions.add(m);
    }

    public List<PositionPnL> simulate()
    {
        int index = 0;

        MarketConditions mc = marketConditions.stream().findAny().get();

        for (Position p : positionList)
        {
            index++;

            double positionPercent = p.getPercentCapitalDeployed();

            double amountInvested = openingCapital.getCapital() * positionPercent / 100;

            double pnl = amountInvested * configuraion.getLev() * (mc.getPercentMove() / 100);

            PositionPnL positionPnl = new PositionPnL(index, p, configuraion, //
                    mc, configuraion.getLev() * (mc.getPercentMove() / 100),
                    pnl,
                    amountInvested,
                    openingCapital.getCapital() + pnl,
                    1d,
                    1d,
                    1d);

            positionPnLList.add(positionPnl);
        }
        return positionPnLList;
    }

    public void addInstrument(Instrument i)
    {
        instruments.add(i);
    }


    public List<PositionPnL> getPositionPnLList()
    {
        return positionPnLList;
    }


    public void addPositionPnL(Position position,
                               Configuration configuraion,
                               OpeningCapital openingCapital,
                               MarketConditions marketConditions)
    {
        double capital = Math.round(openingCapital.getCapital() * (position.getPercentCapitalDeployed() / 100));
        double allowedFirepower = openingCapital.getCapital() * (configuraion.getMaxPercentAllowedPerInstrument() / 100);
        double capitalRemainingFirePower = (openingCapital.getCapital() * configuraion.getPercentAllocationAllowed() / 100) - capital;
        double remainingFirepower = 0;

        double capitalInvestedPerInsrument = 0;

        double totalRemainingFirePower = 0;

        if (getPositionPnLList().size() == 0)
        {
            remainingFirepower = allowedFirepower - capital;

        } else
        {
            capitalInvestedPerInsrument = getPositionPnLList().stream()
                    .filter(pnl -> pnl.getPosition().getInstrument().getName().equals(position.getInstrument().getName()))
                    .mapToDouble(pnl -> pnl.getCurrentPositionEquity()).sum();

            remainingFirepower = allowedFirepower - capitalInvestedPerInsrument - capital;

            totalRemainingFirePower = getPositionPnLList().stream()
                    .mapToDouble(pnl -> pnl.getCurrentPositionEquity()).sum();

            capitalRemainingFirePower = capitalRemainingFirePower - totalRemainingFirePower;

        }
        int index = getPositionPnLList().size()+1;
        double percentPnl = Math.round(marketConditions.getPercentMove() * configuraion.getLev());
        double pnl = capital * (percentPnl / 100);
        positionPnLList.add(new PositionPnL(index, position, configuraion,
                marketConditions, percentPnl, pnl, capital, allowedFirepower,
                remainingFirepower,
                capitalRemainingFirePower, calculateTotalPnl()));

    }

    public double calculateTotalPnl()
    {
        return positionPnLList.stream().mapToDouble(p -> p.getPnl()).sum() + openingCapital.getCapital();
    }

    public void updatePositionPnL(List<PositionPnL> pnl)
    {
        positionPnLList.clear();
        positionPnLList.addAll(pnl);
    }

}
