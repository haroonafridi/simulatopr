package com.hkcapital.portflio.service.importexport.imprt.json.srmatrix;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.repository.srmatrix.SRMatrixFilter;
import com.hkcapital.portflio.service.importexport.Importer;
import com.hkcapital.portflio.service.importexport.ImporterExporterAbstract;
import com.hkcapital.portflio.service.importexport.ImporterExporterDependencies;
import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("srMatrixImporter")
@Slf4j
public class SRMatrixImporter extends ImporterExporterAbstract implements Importer
{
    public SRMatrixImporter(final ImporterExporterDependencies dependencies)
    {
        super(dependencies);
    }

    @Override
    public void importIn()
    {
        String dir = "D:/hk-simulation/strategies-imports/sr-matrix/";
        Set<String> files = Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory() && file.getName().contains("sr-matrix"))
                .map(File::getName)
                .collect(Collectors.toSet());
        for (String file : files)
        {
            try
            {

                List<SRMatrixDTO> srMatrixDTOList =
                        objectReader.forType(new TypeReference<List<SRMatrixDTO>>()
                                {
                                })
                                .readValue(new File(dir + file));


                for (SRMatrixDTO sRMatrixDTO : srMatrixDTOList)
                {
                    final String ticker = sRMatrixDTO.getInstrumentDTO().getInstrumentTicker();

                    Instrument instrument = instService.findByInstrumentTicker(ticker);

                    if (instrument == null)
                    {
                        InstrumentDTO instDTO = sRMatrixDTO.getInstrumentDTO();
                        instrument = instService
                                .addInstrument(Instrument.builder().instrumentTicker(instDTO.getInstrumentTicker())
                                        .etoroInstrumentId(instDTO.getEtoroInstrumentId())
                                        .name(instDTO.getName())
                                        .url(instDTO.getUrl())
                                        .maxSlippage(instDTO.getMaxSlippage())
                                        .active(instDTO.getActive())
                                        .withCandle(instDTO.getWithCandle())
                                        .withBand(instDTO.getWithBand())
                                        .withCandle(instDTO.getWithCandle())
                                        .instrumentDesc(instDTO.getInstrumentDesc())
                                        .build());
                    }
                    SRMatrixFilter srMatrixFilter =
                            SRMatrixFilter
                                    .builder()
                                    .l_s_tolerance(sRMatrixDTO.getL_s_tolerance())
                                    .r_r_tolerance(sRMatrixDTO.getR_r_tolerance())
                                    .r_s_tolerance(sRMatrixDTO.getR_s_tolerance())
                                    .active(sRMatrixDTO.getActive())
                                    .l_r_tolerance(sRMatrixDTO.getL_r_tolerance())
                                    .resistance(sRMatrixDTO.getResistance())
                                    .timeFrameUnit(sRMatrixDTO.getTimeFrameUnit())
                                    .timeFrame(sRMatrixDTO.getTimeFrame())
                                    .stopLoss(sRMatrixDTO.getStopLoss())
                                    .support(sRMatrixDTO.getSupport())
                                    .takeProfit(sRMatrixDTO.getTakeProfit())
                                    .instrument(instrument)
                                    .creationDate(sRMatrixDTO.getCreationDate())
                                    .build();

                    List<SRMatrix> srMatrixList = sRMatrixService.findByFilter(srMatrixFilter);

                    if (srMatrixList == null || srMatrixList.size() == 0)
                    {
                        sRMatrixService
                                .addSRMatrix(SRMatrix.builder()
                                        .support(sRMatrixDTO.getSupport())
                                        .creationDate(sRMatrixDTO.getCreationDate())
                                        .active(sRMatrixDTO.getActive())
                                        .r_s_tolerance(sRMatrixDTO.getR_s_tolerance())
                                        .l_s_tolerance(sRMatrixDTO.getL_s_tolerance())
                                        .r_r_tolerance(sRMatrixDTO.getR_r_tolerance())
                                        .l_r_tolerance(sRMatrixDTO.getL_r_tolerance())
                                        .stopLoss(sRMatrixDTO.getStopLoss())
                                        .takeProfit(sRMatrixDTO.getTakeProfit())
                                        .timeFrameUnit(sRMatrixDTO.getTimeFrameUnit())
                                        .timeFrame(sRMatrixDTO.getTimeFrame())
                                        .resistance(sRMatrixDTO.getResistance())
                                        .instrument(instrument)
                                        .build());
                    }
                }


            } catch (IOException e)
            {
                log.error("Cannot read file sr-matrix");
            }
        }
    }
}
