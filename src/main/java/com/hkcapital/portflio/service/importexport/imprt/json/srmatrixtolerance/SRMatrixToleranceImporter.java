package com.hkcapital.portflio.service.importexport.imprt.json.srmatrixtolerance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.SRMatrixTolerance;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.repository.srmatrix.SRMatrixToleranceFilter;
import com.hkcapital.portflio.service.importexport.Importer;
import com.hkcapital.portflio.service.importexport.ImporterExporterAbstract;
import com.hkcapital.portflio.service.importexport.ImporterExporterDependencies;
import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixToleranceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("sRMatrixToleranceImporter")
@Slf4j
public class SRMatrixToleranceImporter extends ImporterExporterAbstract implements Importer
{
    public SRMatrixToleranceImporter(final ImporterExporterDependencies dependencies)
    {
        super(dependencies);
    }

    @Override
    public void importIn()
    {
        String dir = "D:/hk-simulation/strategies-imports/sr-matrix-tolerance/";
        Set<String> files = Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory() && file.getName().contains("sr-matrix-tolerance"))
                .map(File::getName)
                .collect(Collectors.toSet());
        for (String file : files)
        {
            try
            {

                List<SRMatrixToleranceDTO> sRMatrixToleranceDTOList =
                        objectReader.forType(new TypeReference<List<SRMatrixToleranceDTO>>()
                                {
                                })
                                .readValue(new File(dir + file));


                for (SRMatrixToleranceDTO sRMatrixToleranceDTO : sRMatrixToleranceDTOList)
                {

                    Instrument instrument = instService.findByInstrumentTicker(sRMatrixToleranceDTO.getInstrument().getInstrumentTicker());

                    if (instrument == null)
                    {
                        InstrumentDTO instDTO = sRMatrixToleranceDTO.getInstrument();
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


                    SRMatrixToleranceFilter sRMatrixToleranceFilter =
                            SRMatrixToleranceFilter
                                    .builder()
                                    .r_s_tolerance_percent(sRMatrixToleranceDTO.getR_s_tolerance_percent())
                                    .l_r_tolerance_percent(sRMatrixToleranceDTO.getL_r_tolerance_percent())
                                    .l_s_tolerance_percent(sRMatrixToleranceDTO.getL_s_tolerance_percent())
                                    .r_r_tolerance_percent(sRMatrixToleranceDTO.getR_r_tolerance_percent())
                                    .timeFrameUnit(sRMatrixToleranceDTO.getTimeFrameUnit())
                                    .timeFrame(sRMatrixToleranceDTO.getTimeFrame())
                                    .instrument(instrument)
                                    .stopLossPercent(sRMatrixToleranceDTO.getStopLossPercent())
                                    .takeProfitPercent(sRMatrixToleranceDTO.getTakeProfitPercent())
                                    .active(sRMatrixToleranceDTO.getActive())
                                    .creationDate(sRMatrixToleranceDTO.getCreationDate())
                                    .build();

                    List<SRMatrixTolerance> sRMatrixToleranceList = //
                            sRMatrixToleranceService.findByFilter(sRMatrixToleranceFilter);

                    if (sRMatrixToleranceList == null || sRMatrixToleranceList.size() == 0)
                    {
                        sRMatrixToleranceService
                                .addSRMatrixTolerance(SRMatrixTolerance.builder()
                                        .stopLossPercent(sRMatrixToleranceDTO.getStopLossPercent())
                                        .active(sRMatrixToleranceDTO.getActive())
                                        .l_r_tolerance_percent(sRMatrixToleranceDTO.getL_r_tolerance_percent())
                                        .l_s_tolerance_percent(sRMatrixToleranceDTO.getL_s_tolerance_percent())
                                        .r_r_tolerance_percent(sRMatrixToleranceDTO.getR_r_tolerance_percent())
                                        .r_r_tolerance_percent(sRMatrixToleranceDTO.getR_r_tolerance_percent())
                                        .takeProfitPercent(sRMatrixToleranceDTO.getTakeProfitPercent())
                                        .instrument(instrument)
                                        .timeFrame(sRMatrixToleranceDTO.getTimeFrame())
                                        .timeFrameUnit(sRMatrixToleranceDTO.getTimeFrameUnit())
                                        .creationDate(sRMatrixToleranceDTO.getCreationDate())
                                        .build());
                    }
                }


            } catch (IOException e)
            {
                log.error("Cannot read file sr-matrix-tolerance");
            }
        }
    }
}
