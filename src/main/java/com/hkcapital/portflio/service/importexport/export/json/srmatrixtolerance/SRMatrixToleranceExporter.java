package com.hkcapital.portflio.service.importexport.export.json.srmatrixtolerance;

import com.hkcapital.portflio.model.SRMatrixTolerance;
import com.hkcapital.portflio.service.importexport.*;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixToleranceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
            fileGenerator.fileOf(objectWriter.writeValueAsString(SRMatrixToleranceDTOList),
                    "sr-tolerance.json",null, FileTypes.SR_TOLERANCE);
        } catch (IOException e)
        {
            log.error("Error in reading file r-matrix-tolerance");
        }
    }
}
