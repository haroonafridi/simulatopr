package com.hkcapital.portflio.service.importexport.export.json.srmatrixtolerance;

import com.hkcapital.portflio.model.SRMatrixTolerance;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.importexport.Exporter;
import com.hkcapital.portflio.service.importexport.FileGenerator;
import com.hkcapital.portflio.service.importexport.ImporterExporterAbstract;
import com.hkcapital.portflio.service.importexport.ImporterExporterDependencies;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixToleranceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("srMatrixToleranceExporter")
@Slf4j
public class SRMatrixToleranceExporter //
        extends ImporterExporterAbstract implements Exporter
{
    private final FileGenerator fileGenerator;

    public SRMatrixToleranceExporter(final ImporterExporterDependencies dependencies, //
                                     final @Qualifier("jsonFileGenerator") FileGenerator fileGenerator)
    {
        super(dependencies);
        this.fileGenerator = fileGenerator;
    }

    @Override
    public void export()
    {
        final List<SRMatrixTolerance> sRMatrixToleranceList = sRMatrixToleranceService.findAll();

        final List<SRMatrixToleranceDTO> SRMatrixToleranceDTOList = new ArrayList<>();

        sRMatrixToleranceList.stream().forEach(srMatrixTolerance ->
        {
            SRMatrixToleranceDTOList.add(srMatrixTolerance.buildSRMatrixToleranceDTO());
        });
        try
        {
            final String json = objectWriter.writeValueAsString(SRMatrixToleranceDTOList);
            fileGenerator.fileOf(json, null, null);
            File file = new File("D:/hk-simulation/strategies-export/sr-matrix-tolerance/sr-matrix-tolerance.json");
            if (!file.exists())
            {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();

        } catch (IOException e)
        {
            log.error("Error in reading file r-matrix-tolerance");
        }
    }
}
