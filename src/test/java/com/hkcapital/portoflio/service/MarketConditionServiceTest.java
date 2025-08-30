package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.Instrument;
import com.hkcapital.portoflio.model.MarketConditions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class MarketConditionServiceTest
{
    private static final String C_GOLD = "GOLD";
    private static final String C_NASDAQ = "NASDAQ";
    private final SimulatorTestHelper simulatorTestHelper;

    public MarketConditionServiceTest(SimulatorTestHelper simulatorTestHelper)
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
        Instrument instrument =  //
                simulatorTestHelper.getInstrumentService().addInstrument(new Instrument(C_GOLD));

        Instrument instrumentInserted = simulatorTestHelper.getInstrumentService().findById(instrument.getId());

        Assertions.assertEquals(C_GOLD, instrumentInserted.getName());

        Assertions.assertTrue(simulatorTestHelper.getInstrumentService().findAll().size() == 1);

        MarketConditions marketConditions = //
                simulatorTestHelper //
                        .getMarketConditionsService()
                        .addMarketCondition(new MarketConditions(instrumentInserted, 3400d, 3500d, 1d));

        Assertions.assertTrue(simulatorTestHelper.getInstrumentService().findAll().size() == 1);

        Assertions.assertTrue(simulatorTestHelper.getMarketConditionsService().findAll().size() == 1);

        MarketConditions marketConditionsInserted =  //
                simulatorTestHelper.getMarketConditionsService().findById(marketConditions.getId());

        Assertions.assertEquals(C_GOLD, marketConditionsInserted.getInstrument().getName());

        Assertions.assertEquals(3400d, marketConditionsInserted.getDayLow());

        Assertions.assertEquals(3500d, marketConditionsInserted.getDayHigh());

        Assertions.assertEquals(1d, marketConditionsInserted.getPercentMove());
    }

    @Test
    public void findAllTest()
    {
        simulatorTestHelper //
                .getMarketConditionsService()
                .addMarketCondition(new MarketConditions(simulatorTestHelper.getInstrumentService() //
                        .addInstrument(new Instrument(C_GOLD)), 3400d, 3500d, 1d));
        simulatorTestHelper //
                .getMarketConditionsService()
                .addMarketCondition(new MarketConditions(simulatorTestHelper.getInstrumentService()//
                        .addInstrument(new Instrument(C_NASDAQ)), 22000d, 22500d, 1d));

        Assertions.assertTrue(simulatorTestHelper.getInstrumentService().findAll().size() == 2);

        Assertions.assertTrue(1 == simulatorTestHelper.getInstrumentService()
                .findAll().stream().filter(instrument -> instrument.getName().equals(C_GOLD)).count());

        Assertions.assertTrue(1 == simulatorTestHelper.getInstrumentService()
                .findAll().stream().filter(instrument -> instrument.getName().equals(C_NASDAQ)).count());

        Assertions.assertTrue(2 == simulatorTestHelper.getMarketConditionsService().findAll().size());


    }
}
