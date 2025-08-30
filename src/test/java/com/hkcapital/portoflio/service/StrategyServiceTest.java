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

}
