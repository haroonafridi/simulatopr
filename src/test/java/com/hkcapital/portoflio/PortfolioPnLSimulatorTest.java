package com.hkcapital.portoflio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

class PortfolioPnLSimulatorTest
{
    public static final String GOLD = "GOLD";
    @Test
    public void simulatePortfolio()
    {

        List<Instrument> instrumentList = Arrays.asList(new Instrument(1, GOLD),
                new Instrument(2, "NASDAQ"));

        Configuraion configuration = new Configuraion(1, 15, //
                2, 3, //
                7.5,
                20);

        List<MarketConditions> marketConditions =
                Arrays.asList(new MarketConditions(1, new Instrument(1, GOLD), -1));

        OpeningCapital openingCapital = new OpeningCapital(1, LocalDate.now(), 5165);

        Position p1Gold = new Position(1, new Instrument(1, GOLD), 3.8722);

        List<Position> positionList = Arrays.asList(p1Gold);

        PortfolioPnLSimulator portfolioPnLSimulator =
                new PortfolioPnLSimulator(instrumentList, configuration, marketConditions, positionList , openingCapital);

        List<PositionPnL> pnlList = portfolioPnLSimulator.simulate();

        Assertions.assertTrue(pnlList.size() == 1);

        PositionPnL  pnl = pnlList.stream().findAny().get();

        Assertions.assertEquals(pnl.position().instrument().name() , GOLD);

        Assertions.assertEquals(pnl.currentPositionEquity() , 200);
        Assertions.assertEquals(pnl.pnl() , -40.0);
        Assertions.assertEquals(pnl.portfolioValue() , 5125.0);

    }

}