package com.hkcapital.portflio.service.importexport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.configuration.ConfigurationService;
import com.hkcapital.portflio.service.env.EnvService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.InstrumentMarketStructureConfService;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.service.srmatrix.SRMatrixToleranceService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public abstract class ImporterExporterAbstract
{
    protected final ServiceRegistery serviceRegistery;
    protected final InstrumentService instService;
    protected final MarketConditionsService marketCondService;
    protected final ConfigurationService configService;
    protected final SRMatrixService sRMatrixService;
    protected final SRMatrixToleranceService sRMatrixToleranceService;
    protected final PositionService positionService;
    protected final StrategyService strategyService;
    protected final InstrumentMarketStructureConfService instMrktStrConfSrv;
    protected final EnvService envService;

    protected ObjectReader objectReader = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .reader();

    protected ObjectWriter objectWriter = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .writer()
            .withDefaultPrettyPrinter();

    public ImporterExporterAbstract(final ServiceRegistery serviceRegistery)
    {
        this.serviceRegistery = serviceRegistery;
        this.strategyService = (StrategyService) serviceRegistery.getService(StrategyService.StrategyService);
        this.instService = (InstrumentService) serviceRegistery.getService(InstrumentService.InstrumentService);
        this.marketCondService = (MarketConditionsService) serviceRegistery.getService(MarketConditionsService.MarketConditionsService);
        this.configService = (ConfigurationService) serviceRegistery.getService(ConfigurationService.ConfigurationService);
        this.sRMatrixService = (SRMatrixService) serviceRegistery.getService(SRMatrixService.SRMatrixService);
        this.sRMatrixToleranceService = (SRMatrixToleranceService) serviceRegistery.getService(SRMatrixToleranceService.SRMatrixToleranceService);
        this.positionService = (PositionService) serviceRegistery.getService(PositionService.PositionService);
        this.instMrktStrConfSrv = (InstrumentMarketStructureConfService) serviceRegistery.getService(InstrumentMarketStructureConfService.InstrumentMarketStructureConfService);
        this.envService = (EnvService) serviceRegistery.getService(EnvService.EnvService);
    }
}
