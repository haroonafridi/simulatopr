package com.hkcapital.portflio.service.importexport.export.csv.feed;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.LiveInstrumentFeed;
import com.hkcapital.portflio.service.importexport.FileTypes;
import com.hkcapital.portflio.service.importexport.Literals;
import com.hkcapital.portflio.service.importexport.export.csv.CSVGenerator;
import com.hkcapital.portflio.service.importexport.export.file.csv.CSVFileGenerator;
import com.hkcapital.portflio.service.marketfeed.LiveInstrumentFeedService;
import com.hkcapital.portflio.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static com.hkcapital.portflio.service.importexport.GenerateParameterValidator.validateGenerateParameters;
import static com.hkcapital.portflio.util.DateTimeUtil.asLocalDateTime;

@Service("tickCSVGenerator")
@Slf4j
public class TickCSVGenerator implements CSVGenerator
{
    private final LiveInstrumentFeedService liveInstFeedSrv;
    private final CSVFileGenerator csvFileGenerator;

    public TickCSVGenerator(LiveInstrumentFeedService liveInstFeedSrv,
                            CSVFileGenerator csvFileGenerator)
    {
        this.liveInstFeedSrv = liveInstFeedSrv;
        this.csvFileGenerator = csvFileGenerator;
    }

    @Override
    public int generate(final Instant fromDate, final Instant toDate, final Instrument instrument)
    {
        validateGenerateParameters(fromDate, toDate, instrument);

        final List<LiveInstrumentFeed> feed = liveInstFeedSrv.findByInstrumentIdAndFeedDateBetween
                (
                        instrument.getEtoroInstrumentId(),
                        fromDate,
                        toDate
                );

        csvFileGenerator.fileOf //
                (
                        LiveInstrumentFeedCsvGenerator.generate(feed),  //
                        instrument.getInstrumentTicker() + Literals.UNDER_SCORE.getValue() //
                                + DateTimeUtil.toYearMonthDay(fromDate), //
                        instrument.getInstrumentTicker(),
                        FileTypes.TICK
                );

        log.info("Total csv ticks records created {} , Date from : {} , Date to: {} ", //
                feed.size(),
                asLocalDateTime(fromDate, ZoneOffset.UTC),
                asLocalDateTime(toDate, ZoneOffset.UTC));
        return feed.size();
    }
}







