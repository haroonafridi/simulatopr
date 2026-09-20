package com.hkcapital.portflio.service.importexport.export.csv.candle;

import com.hkcapital.portflio.service.importexport.FileTypes;
import com.hkcapital.portflio.service.importexport.GenerateParameterValidator;
import com.hkcapital.portflio.service.importexport.Literals;
import com.hkcapital.portflio.service.importexport.export.csv.CSVGenerator;
import com.hkcapital.portflio.service.importexport.export.file.csv.CSVFileGenerator;
import com.hkcapital.portflio.util.DateTimeUtil;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static com.hkcapital.portflio.util.DateTimeUtil.asLocalDateTime;

@Slf4j
@Service("candleCSVGenerator")
public class CandleCSVGenerator implements CSVGenerator
{
    private final EtoroCandleService etoroCandleService;
    private final CSVFileGenerator fileGenerator;

    public CandleCSVGenerator(EtoroCandleService etoroCandleService,
                              CSVFileGenerator fileGenerator)
    {
        this.etoroCandleService = etoroCandleService;
        this.fileGenerator = fileGenerator;
    }

    @Override
    public int generate(final Instant fromDate, final Instant toDate, Instrument instrument)
    {

        GenerateParameterValidator.validateGenerateParameters(fromDate, toDate, instrument);


        final List<Candle> candles = etoroCandleService //
                .findByInstrumentIDAndCreationDateTimeBetween//
                        (
                                instrument.getEtoroInstrumentId(), //
                                asLocalDateTime(fromDate, ZoneOffset.UTC),  //
                                asLocalDateTime(toDate, ZoneOffset.UTC)
                        );

        fileGenerator.fileOf(CandleCSVBuilder.buildCSV(candles),
                instrument.getInstrumentTicker() + //
                        Literals.UNDER_SCORE.getValue() + //
                        DateTimeUtil.toYearMonthDay(fromDate), //
                FileTypes.CANDLE);

        log.info("Total csv ticks records created {} , Date from : {} , Date to: {} ", //
                candles.size(),
                asLocalDateTime(fromDate, ZoneOffset.UTC),
                asLocalDateTime(toDate, ZoneOffset.UTC));

        return candles.size();
    }
}
