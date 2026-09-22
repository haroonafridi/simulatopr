package com.hkcapital.portflio.service.importexport.export.json.srmatrix;

import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.service.importexport.*;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("srMatrixExporter")
@Slf4j
public class SRMatrixExporter extends ImporterExporterAbstract implements Exporter
{
    private final FileGenerator fileGenerator;

    public SRMatrixExporter(final ImporterExporterDependencies dependencies,
                            final @Qualifier("jsonFileGenerator") FileGenerator fileGenerator)
    {
        super(dependencies);
        this.fileGenerator = fileGenerator;
    }


    @Override
    public void export()
    {
        final List<SRMatrix> srMatrixList = sRMatrixService.findAll();

        final List<SRMatrixDTO> sRMatrixDTO = new ArrayList<>();

        srMatrixList.stream().forEach(srMatrix ->
        {
            sRMatrixDTO.add(srMatrix.buildDTO());
        });

        try
        {
            fileGenerator.fileOf(objectWriter.writeValueAsString(sRMatrixDTO),
                    "sr-matrix.json",null, FileTypes.SR_MATRIX);
        } catch (IOException e)
        {
            log.error("Error in reading file sr-matrix");
            throw new RuntimeException(e.getMessage());
        }

    }
}
