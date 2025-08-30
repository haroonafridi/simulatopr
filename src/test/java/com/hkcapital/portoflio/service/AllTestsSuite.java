package com.hkcapital.portoflio.service;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class AllTestsSuite
{

    private final SimulatorTestHelper helper;

    public AllTestsSuite(SimulatorTestHelper helper)
    {
        this.helper = helper;
    }


}
