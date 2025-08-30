package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.Instrument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class InstrumentServiceTest
{
    private final SimulatorTestHelper simulatorTestHelper;

    public InstrumentServiceTest(SimulatorTestHelper simulatorTestHelper)
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
        Instrument instrument = //
                simulatorTestHelper //
                        .getInstrumentService()
                        .addInstrument(new Instrument("GOLD"));

        Instrument instrumentInserted = simulatorTestHelper.getInstrumentService().findById(instrument.getId());

        Assertions.assertEquals("GOLD", instrumentInserted.getName());
    }

    @Test
    public void findAllTest()
    {
        simulatorTestHelper.getInstrumentService().addInstrument(new Instrument("GOLD"));
        simulatorTestHelper.getInstrumentService().addInstrument(new Instrument("NASDAQ"));

        Assertions.assertTrue(simulatorTestHelper.getInstrumentService().findAll().size() == 2);

        Assertions.assertTrue(1 == simulatorTestHelper.getInstrumentService()
                .findAll().stream()
                .filter(instrument -> instrument.getName()
                        .equals("GOLD")).count());

        Assertions.assertTrue(1 == simulatorTestHelper.getInstrumentService()
                .findAll().stream()
                .filter(instrument -> instrument.getName()
                        .equals("NASDAQ")).count());

    }
}
