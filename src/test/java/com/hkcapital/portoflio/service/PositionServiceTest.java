package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class PositionServiceTest
{
    private final SimulatorTestHelper simulatorTestHelper;

    public PositionServiceTest(SimulatorTestHelper simulatorTestHelper)
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


        Configuration configuration =  //
                new Configuration(1d, 2, 3, 4d, 5);

        Instrument instrument = simulatorTestHelper.getInstrumentService().addInstrument(new Instrument("GOLD"));

        Strategy strategy = simulatorTestHelper.getStrategyService().addStrategy(new Strategy("GOLD", "GOLD Strategy",  //
                LocalDateTime.of(2025, 8, 30, 10, 10)));

        simulatorTestHelper.getConfigurationService().addConfiguration(configuration);

        MarketConditions marketConditions =  //
                simulatorTestHelper.getMarketConditionsService() //
                        .addMarketCondition(new MarketConditions(instrument, 3400d, 3500d, 1d));


        Position position1 = simulatorTestHelper.getPositionService().add(new Position(1, strategy, configuration, //
                marketConditions, instrument, //
                2d, 5d, 50d, //
                2d, 2d, 5d, 6d, //
                20d));

        Assertions.assertNotNull(position1);

        final Instrument posInstrument = position1.getInstrument();
        final Strategy posStrategy = position1.getStrategy();
        final Configuration posConfiguration = position1.getConfigurtaion();
        final MarketConditions posMarketConditions = position1.getMarketConditions();

        Assertions.assertEquals(1, position1.getRecordIndex());

        Assertions.assertEquals("GOLD", posInstrument.getName());

        Assertions.assertEquals("GOLD", posStrategy.getName());

        Assertions.assertEquals(LocalDateTime.of(2025, 8, 30, 10, 10), posStrategy.getCreationDate());

        Assertions.assertEquals("GOLD Strategy", posStrategy.getDescription());

        Assertions.assertEquals(1, posConfiguration.getPercentAllocationAllowed());

        Assertions.assertEquals(2, posConfiguration.getNoOfInsutrments());

        Assertions.assertEquals(3, posConfiguration.getNoOfPositionsPerInstruments());

        Assertions.assertEquals(4, posConfiguration.getMaxPercentAllowedPerInstrument());

        Assertions.assertEquals(5, posConfiguration.getLev());

        Assertions.assertEquals(3400d, posMarketConditions.getDayLow());
        Assertions.assertEquals(3500d, posMarketConditions.getDayHigh());
        Assertions.assertEquals(1, posMarketConditions.getPercentMove());


        Assertions.assertEquals(2, position1.getPercentPnL());
        Assertions.assertEquals(5, position1.getPercentCapitalDeployed());
        Assertions.assertEquals(50, position1.getPnl());
        Assertions.assertEquals(2, position1.getCurrentPositionEquity());
        Assertions.assertEquals(2, position1.getAllowedFirePower());
        Assertions.assertEquals(5, position1.getRemainingFirepower());
        Assertions.assertEquals(6, position1.getCapitalRemainingFirePower());
        Assertions.assertEquals(20, position1.getPortfolioValue());

    }

}
