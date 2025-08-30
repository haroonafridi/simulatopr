package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.model.Configuration;
import com.hkcapital.portoflio.model.Instrument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class ConfigurationServiceTest
{
    private final SimulatorTestHelper simulatorTestHelper;

    public ConfigurationServiceTest(SimulatorTestHelper simulatorTestHelper)
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
        Configuration configuration = //
                simulatorTestHelper //
                        .getConfigurationService()
                        .addConfiguration(new Configuration(7.0, 2,
                                3, 3.0, 20));

        Configuration configurationInserted = simulatorTestHelper.getConfigurationService().findById(configuration.getId());

        Assertions.assertEquals(7.0, configurationInserted.getPercentAllocationAllowed());
        Assertions.assertEquals(2, configurationInserted.getNoOfInsutrments());
        Assertions.assertEquals(3, configurationInserted.getNoOfPositionsPerInstruments());
        Assertions.assertEquals(3.0, configurationInserted.getMaxPercentAllowedPerInstrument());
        Assertions.assertEquals(20, configurationInserted.getLev());
    }

    @Test
    public void findAllTest()
    {

        Configuration configuration1 =  new Configuration(7.0, 2,
                3, 3.0, 20);
        Configuration configuration2 =  new Configuration(8.0, 2,
                3, 5.0, 20);
        simulatorTestHelper.getConfigurationService().addConfiguration(configuration1);
        simulatorTestHelper.getConfigurationService().addConfiguration(configuration2);
        Assertions.assertTrue(simulatorTestHelper.getConfigurationService().findAll().size() == 2);

    }
}
