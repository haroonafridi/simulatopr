package com.hkcapital.portflio.service.importexport.export.csv;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.registry.Service;

import java.time.Instant;

public interface CSVGenerator extends Service
{
    int generate(final Instant fromDate, final Instant toDate, final Instrument instrument);
}
