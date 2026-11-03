package com.hkcapital.portflio.service.importexport.imprt.json.indicator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.TradingIndicator;
import com.hkcapital.portflio.repository.tradingindicators.TradingIndicatorsFilter;
import com.hkcapital.portflio.service.importexport.Importer;
import com.hkcapital.portflio.service.importexport.ImporterExporterAbstract;
import com.hkcapital.portflio.service.importexport.ImporterExporterDependencies;
import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import com.hkcapital.portflio.service.tradingindicators.dto.TradingIndicatorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("indicatorImporter")
@Slf4j
public class TradingIndicatorImporter extends ImporterExporterAbstract implements Importer
{
    public TradingIndicatorImporter(final ImporterExporterDependencies dependencies)
    {
        super(dependencies);
    }

    @Override
    public void importIn()
    {
        final String dir = dataPathConfig.getImportStrategy();
        Set<String> files = Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory() && file.getName().contains("indicator"))
                .map(File::getName)
                .collect(Collectors.toSet());
        for (String file : files)
        {
            try
            {

                List<TradingIndicatorDTO> tradingIndicatorDTOList =
                        objectReader.forType(new TypeReference<List<TradingIndicatorDTO>>()
                                {
                                })
                                .readValue(new File(dir + file));


                for (TradingIndicatorDTO indicatorDTO : tradingIndicatorDTOList)
                {
                    final String ticker = indicatorDTO.getInstrument().getInstrumentTicker();

                    Instrument instrument = instService.findByInstrumentTicker(ticker);

                    if (instrument == null)
                    {
                        InstrumentDTO instDTO = indicatorDTO.getInstrument();
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
                    TradingIndicatorsFilter filter =
                            TradingIndicatorsFilter
                                    .builder()
                                    .period(indicatorDTO.getPeriod())
                                    .instrument(instrument)
                                    .name(indicatorDTO.getName())
                                    .timeFrameUnit(indicatorDTO.getTimeFrameUnit())
                                    .timeFrame(indicatorDTO.getTimeFrame())
                                    .build();

                    List<TradingIndicator> indicators = indicatorsService.findByFilter(filter);

                    if (indicators == null || indicators.size() == 0)
                    {
                        indicatorsService
                                .save(TradingIndicator.builder()
                                        .name(indicatorDTO.getName())
                                        .period(indicatorDTO.getPeriod())
                                        .timeFrameUnit(indicatorDTO.getTimeFrameUnit())
                                        .timeFrame(indicatorDTO.getTimeFrame())
                                        .instrument(instrument)
                                        .build());
                    }
                }


            } catch (IOException e)
            {
                log.error("Cannot read file sr-matrix");
                throw new RuntimeException(e);
            }
        }
    }
}
