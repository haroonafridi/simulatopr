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


    public void addInstrument(Instrument i)
    {
        instruments.add(i);
    }


    public List<Position> getPositionPnLList()
    {
        return positionPnLList;
    }



}
