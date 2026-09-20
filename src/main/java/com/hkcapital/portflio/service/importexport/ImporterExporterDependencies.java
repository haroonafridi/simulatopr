package com.hkcapital.portflio.service.importexport;

import com.hkcapital.portflio.service.configuration.ConfigurationService;
import com.hkcapital.portflio.service.env.EnvService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.InstrumentMarketStructureConfService;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.service.srmatrix.SRMatrixToleranceService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImporterExporterDependencies
{
    final InstrumentService instService;
    final MarketConditionsService marketCondService;
    final ConfigurationService configService;
    final SRMatrixService sRMatrixService;
    final SRMatrixToleranceService sRMatrixToleranceService;
    final PositionService positionService;
    final StrategyService strategyService;
    final InstrumentMarketStructureConfService instMrktStrConfSrv;
    final EnvService envService;

}
