package com.hkcapital.portflio.service.importexport.export.json.candles;

import com.hkcapital.portflio.market.indicators.CandleDto;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.SRMatrix;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.importexport.*;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class CandleExporter extends ImporterExporterAbstract implements Exporter
{
    private final FileGenerator fileGenerator;
    private final EtoroCandleService candleService;
    private  Instrument instrument;
    private  LocalDateTime dateFrom;
    private  LocalDateTime dateTo;

    public CandleExporter(final ImporterExporterDependencies dependencies,
                          final @Qualifier("jsonFileGenerator") FileGenerator fileGenerator,
                          final EtoroCandleService candleService,
                          final Instrument instrument,
                          final LocalDateTime dateFrom,
                          final LocalDateTime dateTo
    )
    {
        super(dependencies);
        this.fileGenerator = fileGenerator;
        this.candleService = candleService;
        this.instrument = instrument;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    @Override
    public void export()
    {
        final List<CandleDto> candleList = candleService
                .findCandleDtoByInstrumentIDAndCreationDateTimeBetween(instrument, dateFrom, dateTo);
        try
        {
            fileGenerator.fileOf(objectWriter.writeValueAsString(candleList),
                    "candles.json",null, FileTypes.CANDLE);
        } catch (IOException e)
        {
            log.error("Error in reading file sr-matrix");
            throw new RuntimeException(e.getMessage());
        }
    }

}
