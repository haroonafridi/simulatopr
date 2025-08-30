package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.*;
import com.hkcapital.portoflio.repository.PositionRepository;

import java.util.ArrayList;
import java.util.List;



public class PortfolioPnLServiceImpl
{
    private final PositionRepository positionPnLRepository;
    private final List<Instrument> instruments;
    private final Configuration configuraion;

    private final List<MarketConditions> marketConditions;

    private final OpeningCapital openingCapital;

    private List<Position> positionPnLList = new ArrayList<>();


    public PortfolioPnLServiceImpl(final List<Instrument> instruments, final Configuration configuraion, //
                                   final List<MarketConditions> marketConditions,//
                                   final OpeningCapital openingCapital,
                                   final PositionRepository positionPnLRepository)
    {
        this.instruments = instruments;
        this.configuraion = configuraion;
        this.marketConditions = marketConditions;
        this.openingCapital = openingCapital;
        this.positionPnLRepository = positionPnLRepository;
    }



    public void addMarkerCondition(MarketConditions m)
    {
        marketConditions.add(m);
    }

//    public List<PositionPnL> simulate()
//    {
//        int index = 0;
//
//        MarketConditions mc = marketConditions.stream().findAny().get();
//
//        for (Position p : positionList)
//        {
//            index++;
//
//            double positionPercent = p.getPercentCapitalDeployed();
//
//            double amountInvested = openingCapital.getCapital() * positionPercent / 100;
//
//            double pnl = amountInvested * configuraion.getLev() * (mc.getPercentMove() / 100);
//
//            PositionPnL positionPnl = new PositionPnL(index, configuraion, //
//                    mc, null, null, configuraion.getLev() * (mc.getPercentMove() / 100),
//                    pnl,
//                    amountInvested,
//                    openingCapital.getCapital() + pnl,
//                    1d,
//                    1d,
//                    1d);
//
//            positionPnLList.add(positionPnl);
//        }
//        return positionPnLList;
//    }

    public void addInstrument(Instrument i)
    {
        instruments.add(i);
    }


    public List<Position> getPositionPnLList()
    {
        return positionPnLList;
    }


    public void addPositionPnL(
                               Configuration configuraion,
                               OpeningCapital openingCapital,
                               MarketConditions marketConditions)
    {
        double capital = 2;//Math.round(openingCapital.getCapital() * (getPercentCapitalDeployed() / 100));
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
                    .filter(pnl -> pnl.getInstrument().getName().equals(pnl.getInstrument().getName()))
                    .mapToDouble(pnl -> pnl.getCurrentPositionEquity()).sum();

            remainingFirepower = allowedFirepower - capitalInvestedPerInsrument - capital;

            totalRemainingFirePower = getPositionPnLList().stream()
                    .mapToDouble(pnl -> pnl.getCurrentPositionEquity()).sum();

            capitalRemainingFirePower = capitalRemainingFirePower - totalRemainingFirePower;

        }
        int index = getPositionPnLList().size() + 1;
        double percentPnl = Math.round(marketConditions.getPercentMove() * configuraion.getLev());
        double pnl = capital * (percentPnl / 100);
        positionPnLList.add(new Position(index,null, configuraion,
                marketConditions, null, null, percentPnl, pnl, capital, allowedFirepower,
                remainingFirepower,
                capitalRemainingFirePower, calculateTotalPnl()));

    }

    public double calculateTotalPnl()
    {
        return positionPnLList.stream().mapToDouble(p -> p.getPnl()).sum() + openingCapital.getCapital();
    }

    public void updatePositionPnL(List<Position> pnl)
    {
        positionPnLList.clear();
        positionPnLList.addAll(pnl);
    }

}
