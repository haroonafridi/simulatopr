package com.hkcapital.portflio.service.importexport.export.json.marketstructureconf;

import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.importexport.Exporter;
import com.hkcapital.portflio.service.importexport.FileGenerator;
import com.hkcapital.portflio.service.importexport.ImporterExporterAbstract;
import com.hkcapital.portflio.service.importexport.ImporterExporterDependencies;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.dto.InstrumentStructureConfDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("marketStructureConfigurationExporter")
@Slf4j
public class MarketStructureConfigurationExporter extends ImporterExporterAbstract implements Exporter
{
    private final FileGenerator fileGenerator;

    public MarketStructureConfigurationExporter
            (
                    final ImporterExporterDependencies dependencies,
                    final @Qualifier("jsonFileGenerator") FileGenerator fileGenerator
            )
    {
        super(dependencies);
        this.fileGenerator = fileGenerator;
    }

    @Override
    public void export()
    {
        List<InstrumentMarketStructureConf> instMrktStrtrConfs = instMrktStrConfSrv.findAll();

        List<InstrumentStructureConfDTO> instMrktStrtrConfsDtos = new ArrayList<>();

        instMrktStrtrConfs.stream().forEach(markConf ->
        {
            instMrktStrtrConfsDtos.add(markConf.buildDto());
        });
        try
        {
            final String json = objectWriter.writeValueAsString(instMrktStrtrConfsDtos);
            fileGenerator.fileOf(json, null, null);
            File file = new File("D:/hk-simulation/strategies-export/instrument-market-structure-conf/instrument-market-structure-conf.json");

            if (!file.exists())
            {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e)
        {
            log.error("Error in reading file instrument-market-structure-conf");
        }
    }
}
