package com.hkcapital.portflio.service.importexport.export.json.strategy;

import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.service.importexport.*;
import com.hkcapital.portflio.service.importexport.export.json.marketstructureconf.MarketStructureConfigurationExporter;
import com.hkcapital.portflio.service.importexport.export.json.srmatrix.SRMatrixExporter;
import com.hkcapital.portflio.service.importexport.export.json.srmatrixtolerance.SRMatrixToleranceExporter;
import com.hkcapital.portflio.service.strategy.dto.StrategyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service("strategyExporter")
public class StrategyExporter extends ImporterExporterAbstract implements Exporter
{
    private Strategy strategy;

    private final MarketStructureConfigurationExporter mrktStrtureConfigExporter;
    private final SRMatrixToleranceExporter srMatrixTolranceExporter;
    private final SRMatrixExporter srMatrixExporter;
    private final FileGenerator fileGenerator;

    public StrategyExporter(final ImporterExporterDependencies dependencies,
                            final MarketStructureConfigurationExporter mrktStrtureConfigExporter,
                            final SRMatrixToleranceExporter srMatrixTolranceExporter,
                            final SRMatrixExporter srMatrixExporter,
                            final @Qualifier("jsonFileGenerator") FileGenerator fileGenerator)
    {
        super(dependencies);
        this.mrktStrtureConfigExporter = mrktStrtureConfigExporter;
        this.srMatrixTolranceExporter = srMatrixTolranceExporter;
        this.srMatrixExporter = srMatrixExporter;
        this.fileGenerator = fileGenerator;
    }

    @Override
    public void export()
    {
        mrktStrtureConfigExporter.export();
        srMatrixTolranceExporter.export();
        srMatrixExporter.export();
        try
        {
            StrategyDTO strategyDTO = strategyService.findById(strategy.getId()).buildStrategyDTO();
            fileGenerator.fileOf(objectWriter.writeValueAsString(strategyDTO),
                    strategyDTO.getName() + "-strategy.json", null, FileTypes.STRATEGY);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public void setStrategy(Strategy strategy)
    {
        this.strategy = strategy;
    }
}
