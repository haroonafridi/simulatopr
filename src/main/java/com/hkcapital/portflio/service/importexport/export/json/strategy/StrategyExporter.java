package com.hkcapital.portflio.service.importexport.export.json.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.importexport.Exporter;
import com.hkcapital.portflio.service.importexport.FileGenerator;
import com.hkcapital.portflio.service.importexport.ImporterExporterAbstract;
import com.hkcapital.portflio.service.importexport.ImporterExporterDependencies;
import com.hkcapital.portflio.service.importexport.export.json.marketstructureconf.MarketStructureConfigurationExporter;
import com.hkcapital.portflio.service.importexport.export.json.srmatrix.SRMatrixExporter;
import com.hkcapital.portflio.service.importexport.export.json.srmatrixtolerance.SRMatrixToleranceExporter;
import com.hkcapital.portflio.service.strategy.StrategyService;
import com.hkcapital.portflio.service.strategy.dto.StrategyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
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
        this.mrktStrtureConfigExporter.export();
        this.srMatrixTolranceExporter.export();
        this.srMatrixExporter.export();
        try
        {
            StrategyDTO strategyDTO = strategy.buildStrategyDTO();
            final String json = objectWriter.writeValueAsString(strategyDTO);
            try
            {
                fileGenerator.fileOf(json, null, null);
                File file = new File("D:/hk-simulation/strategies-export/" + strategyDTO.getName() + "-strategy.json");

                if (!file.exists())
                {
                    file.createNewFile();
                }
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(json);
                fileWriter.close();

            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setStrategy(Strategy strategy)
    {
        this.strategy = strategy;
    }
}
