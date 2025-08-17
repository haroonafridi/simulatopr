package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class PortfolioPnLService
{
    private final List<Instrument> instruments;
    private final Configuraion configuraion;

    private final List<MarketConditions> marketConditions;

    private final List<Position> positionList;

    private final OpeningCapital openingCapital;

    private List<PositionPnL> positionPnLList = new ArrayList<>();

    public PortfolioPnLService(final List<Instrument> instruments, final Configuraion configuraion, //
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

        MarketConditions mc =  marketConditions.stream().findAny().get();

        for (Position p : positionList)
        {
            index++;

            double positionPercent = p.percentCapitalDeployed();

            double amountInvested = openingCapital.capital() * positionPercent / 100;

            double pnl = amountInvested * configuraion.lev()* (mc.percentMove() / 100);

            PositionPnL positionPnl = new PositionPnL(index, p, configuraion, //
                    mc, round(configuraion.lev()* (mc.percentMove() / 100)), round(pnl),
                    round(amountInvested), round(openingCapital.capital()+pnl));

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

}
