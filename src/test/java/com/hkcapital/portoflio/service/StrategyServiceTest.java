package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class StrategyServiceTest
{
    private final SimulatorTestHelper simulatorTestHelper;

    public StrategyServiceTest(SimulatorTestHelper simulatorTestHelper)
    {
        this.simulatorTestHelper = simulatorTestHelper;
    }

    @BeforeEach
    public void cleanDb()
    {
        simulatorTestHelper.cleanDb();
    }

    @Test
    public void testInsert()
    {
        Strategy goldStrategy = //
                simulatorTestHelper //
                        .getStrategyService()
                        .addStrategy(new Strategy("GOLD", "GOLD Strategy",  //
                                LocalDateTime.of(2025, 8, 30, 10, 10)));

        Strategy goldInserted = simulatorTestHelper.getStrategyService().findById(goldStrategy.getId());

        Assertions.assertNotNull(goldInserted);

        Strategy nasdaqStrategy = //
                simulatorTestHelper //
                        .getStrategyService()
                        .addStrategy(new Strategy("NASDAQ", "NASDAQ Strategy",  //
                                LocalDateTime.of(2025, 8, 30, 10, 11)));

        Strategy nasdaqInserted = simulatorTestHelper.getStrategyService().findById(nasdaqStrategy.getId());

        Assertions.assertNotNull(nasdaqInserted);

        Assertions.assertEquals("GOLD", goldInserted.getName());

        Assertions.assertEquals("GOLD Strategy", goldInserted.getDescription());

        Assertions.assertEquals("NASDAQ", nasdaqInserted.getName());

        Assertions.assertEquals("NASDAQ Strategy", nasdaqInserted.getDescription());

    }

    @Test
    void testInsertMany()
    {

        Configuration configuration =  //
                new Configuration(1d, 2, 3, 4d, 5);

        Instrument instrument = simulatorTestHelper.getInstrumentService().addInstrument(new Instrument("GOLD"));


        simulatorTestHelper.getConfigurationService().addConfiguration(configuration);

        MarketConditions marketConditions =  //
                simulatorTestHelper.getMarketConditionsService() //
                        .addMarketCondition(new MarketConditions(instrument, 3400d, 3500d, 1d));


        Strategy strategy = new Strategy("GOLD", "GOLD Strategy",  //
                LocalDateTime.of(2025, 8, 30, 10, 10));

        Position p1 = new Position(1, strategy, configuration, //
                marketConditions, instrument, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d);

        Position p2 = new Position(2, strategy, configuration, //
                marketConditions, instrument, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d);

        Position p3 = new Position(3, strategy, configuration, //
                marketConditions, instrument, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d);

        Position p4 = new Position(4, strategy, configuration, //
                marketConditions, instrument, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d);

        strategy.setPositionPnLList(Arrays.asList(p1, p2, p3, p4));

        Strategy strategyInserted = simulatorTestHelper.getStrategyService().addStrategy(strategy);

        Assertions.assertEquals(4, strategyInserted.getPositionPnLList().stream().count());

    }



    @Test
    void testGoldAndNasdaqStrategy()
    {

        Configuration configuration =  //
                new Configuration(1d, 2, 3, 4d, 5);

        Instrument gold = simulatorTestHelper.getInstrumentService().addInstrument(new Instrument("GOLD"));

        Instrument nasdaq = simulatorTestHelper.getInstrumentService().addInstrument(new Instrument("NASDAQ"));


        simulatorTestHelper.getConfigurationService().addConfiguration(configuration);

        MarketConditions marketConditionsGold =  //
                simulatorTestHelper.getMarketConditionsService() //
                        .addMarketCondition(new MarketConditions(gold, 3400d, 3500d, 1d));

        MarketConditions marketConditionsNasdaq =  //
                simulatorTestHelper.getMarketConditionsService() //
                        .addMarketCondition(new MarketConditions(nasdaq, 22000d, 23000d, 1d));



        Strategy strategyGold = new Strategy("GOLD", "GOLD Strategy",  //
                LocalDateTime.of(2025, 8, 30, 10, 10));

        Strategy strategyNasdaq = new Strategy("NASDAQ", "NASDAQ Strategy",  //
                LocalDateTime.of(2025, 8, 30, 10, 10));

        Position p1Gold = new Position(1, strategyGold, configuration, //
                marketConditionsGold, gold, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d);

        Position p2Gold = new Position(2, strategyGold, configuration, //
                marketConditionsGold, gold, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d);

        Position p3Gold = new Position(3, strategyGold, configuration, //
                marketConditionsGold, gold, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d);

        Position p4Gold = new Position(4, strategyGold, configuration, //
                marketConditionsGold, gold, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d);

        strategyGold.setPositionPnLList(Arrays.asList(p1Gold, p2Gold, p3Gold, p4Gold));

        Position p1Nasdaq = new Position(1, strategyNasdaq, configuration, //
                marketConditionsNasdaq, gold, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d);

        Position p2Nasdaq = new Position(1, strategyNasdaq, configuration, //
                marketConditionsNasdaq, gold, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d);
        Position p3Nasdaq = new Position(1, strategyNasdaq, configuration, //
                marketConditionsNasdaq, gold, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d);
        Position p4Nasdaq = new Position(1, strategyNasdaq, configuration, //
                marketConditionsNasdaq, gold, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d);

        strategyNasdaq.setPositionPnLList(Arrays.asList(p1Nasdaq, p2Nasdaq, p3Nasdaq, p4Nasdaq));

        Strategy strategyInsertedGold = simulatorTestHelper.getStrategyService().addStrategy(strategyGold);
        Strategy strategyInsertedNasdaq = simulatorTestHelper.getStrategyService().addStrategy(strategyNasdaq);

        Assertions.assertEquals(4, strategyInsertedGold.getPositionPnLList().stream().count());
        Assertions.assertEquals(4, strategyInsertedNasdaq.getPositionPnLList().stream().count());
    }





}
