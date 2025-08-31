package com.hkcapital.portoflio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CleanDbTest
{
    private final SimulatorTestHelper simulatorTestHelper;

    public CleanDbTest(SimulatorTestHelper simulatorTestHelper)
    {
        this.simulatorTestHelper = simulatorTestHelper;
    }

    @BeforeEach
    public void cleanDb()
    {
        simulatorTestHelper.cleanDb();
    }

    @Test
    public void clean()
    {
        System.out.println("clean db");
    }


}
