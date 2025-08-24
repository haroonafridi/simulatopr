package com.hkcapital.portoflio.simulation;

import com.hkcapital.portoflio.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class SimulationData
{
    public static final String GOLD = "GOLD";
    public static final String NASDAQ = "NASDAQ";

    public static final Position P1_GOLD = new Position(1, new Instrument(1, SimulationData.GOLD), 3.8722);

    public static final Configuraion CONFIGURAION = new Configuraion(1, 15d, //
            2, 3, //
            7.5,
            20);
    public static final List<Position> POSITIONS = Arrays.asList(P1_GOLD, P1_GOLD, P1_GOLD);

    public static final List<Instrument> INSTRUMENTS = Arrays.asList(new Instrument(1, GOLD),
            new Instrument(2, NASDAQ));

    public static final OpeningCapital OPENING_CAPITAL = new OpeningCapital(1, LocalDate.now(), 5165);

    public static final List<MarketConditions> MARKET_CONDITIONS =
            Arrays.asList(new MarketConditions(1, new Instrument(1, SimulationData.GOLD),3372d , 3354d, -1d));
    public static final MarketConditions MARKET_CONDITION= new MarketConditions(1, new Instrument(1, SimulationData.GOLD), 3372d
            , 3354d, -1d);

    public static  final PositionPnL POSITION_PN_L =  new PositionPnL(4, P1_GOLD, CONFIGURAION, MARKET_CONDITION, 100d, 100d,
   100d, 100d, 1d,1d , 1d);

}
