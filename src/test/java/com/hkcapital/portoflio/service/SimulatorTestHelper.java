package com.hkcapital.portoflio.service;

import org.springframework.stereotype.Component;

@Component
public class SimulatorTestHelper
{
    private final InstrumentService instrumentService;
    private final ConfigurationService configurationService;

    private final MarketConditionsService marketConditionsService;

    private final PositionService positionService;

    private final StrategyService strategyService;

    public SimulatorTestHelper(InstrumentService instrumentService, //
                               ConfigurationService configurationService,  //
                               MarketConditionsService marketConditionsService, //
                               PositionService positionPnLService,  //
                               StrategyService strategyService)
    {
        this.instrumentService = instrumentService;
        this.configurationService = configurationService;
        this.marketConditionsService = marketConditionsService;
        this.positionService = positionPnLService;
        this.strategyService = strategyService;
    }

    public InstrumentService getInstrumentService()
    {
        return instrumentService;
    }

    public ConfigurationService getConfigurationService()
    {
        return configurationService;
    }

    public MarketConditionsService getMarketConditionsService()
    {
        return marketConditionsService;
    }

    public PositionService getPositionService()
    {
        return positionService;
    }

    public StrategyService getStrategyService()
    {
        return strategyService;
    }

    public void cleanDb() {
        strategyService.removeAll();
        positionService.removeAll();
        marketConditionsService.removeAll();
        configurationService.removeAll();
        instrumentService.removeAll();
    }
}
