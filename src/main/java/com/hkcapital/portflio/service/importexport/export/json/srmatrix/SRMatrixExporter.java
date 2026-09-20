package com.hkcapital.portflio.service.importexport.export.json.srmatrix;

import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.importexport.Exporter;
import com.hkcapital.portflio.service.importexport.FileGenerator;
import com.hkcapital.portflio.service.importexport.ImporterExporterAbstract;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SRMatrixExporter extends ImporterExporterAbstract implements Exporter
{
    private final FileGenerator fileGenerator;

    public SRMatrixExporter(final ServiceRegistery serviceRegistery,
                            final FileGenerator fileGenerator)
    {
        super(serviceRegistery);
        this.fileGenerator = fileGenerator;
    }


    @Override
    public void export()
    {
        List<SRMatrix> srMatrixList = sRMatrixService.findAll();

        List<SRMatrixDTO> sRMatrixDTO = new ArrayList<>();

        srMatrixList.stream().forEach(srMatrix ->
        {
            sRMatrixDTO.add(srMatrix.buildDTO());
        });


        try
        {
            final String json = objectWriter.writeValueAsString(sRMatrixDTO);
            fileGenerator.fileOf(json, null, null);
            File file = new File("D:/hk-simulation/strategies-export/sr-matrix/sr-matrix.json");
            if(!file.exists())
            {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e)
        {
            log.error("Error in reading file sr-matrix");
        }

    }
}
