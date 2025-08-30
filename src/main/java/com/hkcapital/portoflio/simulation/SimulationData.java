package com.hkcapital.portoflio.simulation;

import com.hkcapital.portoflio.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class SimulationData
{
    public static final String GOLD = "GOLD";
    public static final String NASDAQ = "NASDAQ";



    public static final Configuration CONFIGURAION = new Configuration( 15d, //
            2, 3, //
            7.5,
            20);

    public static final List<Instrument> INSTRUMENTS = Arrays.asList(new Instrument( GOLD),
            new Instrument( NASDAQ));

    public static final OpeningCapital OPENING_CAPITAL = new OpeningCapital(1, LocalDate.now(), 5165);

    public static final List<MarketConditions> MARKET_CONDITIONS =
            Arrays.asList(new MarketConditions( new Instrument( SimulationData.GOLD),3372d , 3354d, -1d));
    public static final MarketConditions MARKET_CONDITION= new MarketConditions( new Instrument( SimulationData.GOLD), 3372d
            , 3354d, -1d);

    public static  final Position POSITION_PN_L =  new Position(4,  null, CONFIGURAION,  MARKET_CONDITION, null, null, 100d, 100d,
   100d, 100d, 1d,1d , 1d);

}
