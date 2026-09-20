package com.hkcapital.portflio.service.importexport.imprt.json.marketsructureconf;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.InstrumentMarketStructureConf;
import com.hkcapital.portflio.repository.instrumentmarketstructureconf.InstrumentMarketStructureConfFilter;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.importexport.Importer;
import com.hkcapital.portflio.service.importexport.ImporterExporterAbstract;
import com.hkcapital.portflio.service.importexport.ImporterExporterDependencies;
import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import com.hkcapital.portflio.service.instrumentmarketstructureconf.dto.InstrumentStructureConfDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Slf4j
@Service("marketStructureConfigurationImporter")
public class MarketStructureConfigurationImporter //
        extends ImporterExporterAbstract implements Importer
{
    public MarketStructureConfigurationImporter(final ImporterExporterDependencies dependencies)
    {
        super(dependencies);
    }

    @Override
    public void importIn()
    {
        String dir = "D:/hk-simulation/strategies-imports/instrument-market-structure-conf/";
        try
        {
            Set<String> files = Stream.of(new File(dir).listFiles())
                    .filter(file -> !file.isDirectory() && file.getName().contains("instrument-market-structure-conf"))
                    .map(File::getName)
                    .collect(Collectors.toSet());
            for (String file : files)
            {
                try
                {

                    List<InstrumentStructureConfDTO> instStrConfDTOs =
                            objectReader.forType(new TypeReference<List<InstrumentStructureConfDTO>>()
                                    {
                                    })
                                    .readValue(new File(dir + file));


                    for (InstrumentStructureConfDTO instStrConfDTO : instStrConfDTOs)
                    {

                        Instrument instrument = instService.findByInstrumentTicker(instStrConfDTO.getInstrumentDto().getInstrumentTicker());

                        if (instrument == null)
                        {
                            InstrumentDTO instDTO = instStrConfDTO.getInstrumentDto();
                            instrument = instService
                                    .addInstrument(Instrument.builder().instrumentTicker(instDTO.getInstrumentTicker())
                                            .etoroInstrumentId(instDTO.getEtoroInstrumentId())
                                            .name(instDTO.getName())
                                            .url(instDTO.getUrl())
                                            .maxSlippage(instDTO.getMaxSlippage())
                                            .active(instDTO.getActive())
                                            .withCandle(instDTO.getWithCandle())
                                            .withFeed(instDTO.getWithFeed())
                                            .withBand(instDTO.getWithBand())
                                            .withCandle(instDTO.getWithCandle())
                                            .instrumentDesc(instDTO.getInstrumentDesc())
                                            .build());
                        }


                        InstrumentMarketStructureConfFilter filter =
                                InstrumentMarketStructureConfFilter
                                        .builder()
                                        .structureName(instStrConfDTO.getStructureName())
                                        .build();

                        List<InstrumentMarketStructureConf> instMrktStrConf = //
                                instMrktStrConfSrv.findByFilter(filter);

                        if (instMrktStrConf == null || instMrktStrConf.size() == 0)
                        {
                            instMrktStrConfSrv.add(InstrumentMarketStructureConf.builder()
                                    .structureName(instStrConfDTO.getStructureName())
                                    .marketOrder(instStrConfDTO.getMarketOrder())
                                    .sub(instStrConfDTO.getSub())
                                    .intrvl(instStrConfDTO.getIntrvl())
                                    .module(instStrConfDTO.getModule())
                                    .timeFrameUnit(instStrConfDTO.getTimeFrameUnit())
                                    .instrument(instrument)
                                    .active(instStrConfDTO.isActive())
                                    .creationDate(instStrConfDTO.getCreationDate())
                                    .timeFrame(instStrConfDTO.getTimeFrame())
                                    .build());

                        }
                    }


                } catch (IOException e)
                {
                    log.error("Cannot read file instrument-market-structure-conf");
                }
            }
        } catch (Exception e)
        {
            log.error("Cannot read file instrument-market-structure-conf");
        }
    }
}
