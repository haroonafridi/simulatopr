package com.hkcapital.portflio.service.export.csv.candle;

import com.hkcapital.portflio.util.DateTimeUtil;
import com.hkcapital.portflio.model.Candle;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.export.FileTypes;
import com.hkcapital.portflio.service.export.csv.CSVGenerator;
import com.hkcapital.portflio.service.export.file.csv.CSVFileGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static com.hkcapital.portflio.util.DateTimeUtil.asLocalDateTime;
import static com.hkcapital.portflio.service.export.GenerateParameterValidator.validateGenerateParameters;
import static com.hkcapital.portflio.service.export.Literals.UNDER_SCORE;

@Slf4j
@Service
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

        validateGenerateParameters(fromDate, toDate, instrument);


        final List<Candle> candles = etoroCandleService //
                .findByInstrumentIDAndCreationDateTimeBetween//
                        (
                                instrument.getEtoroInstrumentId(), //
                                asLocalDateTime(fromDate, ZoneOffset.UTC),  //
                                asLocalDateTime(toDate, ZoneOffset.UTC)
                        );

        fileGenerator.fileOf(CandleCSVBuilder.buildCSV(candles),
                instrument.getInstrumentTicker() + //
                        UNDER_SCORE.getValue() + //
                        DateTimeUtil.toYearMonthDay(fromDate), //
                FileTypes.CANDLE.getType());

        log.info("Total csv ticks records created {} , Date from : {} , Date to: {} ", //
                candles.size(),
                asLocalDateTime(fromDate, ZoneOffset.UTC),
                asLocalDateTime(toDate, ZoneOffset.UTC));

        return candles.size();
    }
}
