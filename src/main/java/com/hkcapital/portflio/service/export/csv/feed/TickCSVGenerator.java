package com.hkcapital.portflio.service.export.csv.feed;

import com.hkcapital.portflio.market.structure.DateTimeUtil;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.model.LiveInstrumentFeed;
import com.hkcapital.portflio.service.export.FileTypes;
import com.hkcapital.portflio.service.export.Literals;
import com.hkcapital.portflio.service.export.csv.CSVGenerator;
import com.hkcapital.portflio.service.export.file.csv.CSVFileGenerator;
import com.hkcapital.portflio.service.marketfeed.LiveInstrumentFeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static com.hkcapital.portflio.market.structure.DateTimeUtil.asLocalDateTime;
import static com.hkcapital.portflio.service.export.GenerateParameterValidator.validateGenerateParameters;

@Service
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

        final List<LiveInstrumentFeed> feed = liveInstFeedSrv.findByFeedDateBetween(fromDate, toDate);

        csvFileGenerator.fileOf //
                (
                        LiveInstrumentFeedCsvGenerator.generate(feed),  //
                        instrument.getInstrumentTicker() + Literals.UNDER_SCORE.getValue() + DateTimeUtil.toYearMonthDay(fromDate), //
                        FileTypes.TICK.getType()
                );

        log.info("Total csv ticks records created {} , Date from : {} , Date to: {} ", //
                feed.size(),
                asLocalDateTime(fromDate, ZoneOffset.UTC),
                asLocalDateTime(toDate, ZoneOffset.UTC));
        return feed.size();
    }
}







