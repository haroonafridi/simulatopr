package com.hkcapital.portflio.service.export;

import com.hkcapital.portflio.model.Instrument;

import java.time.Instant;
import java.util.Objects;

public final class GenerateParameterValidator
{

    public static void validateGenerateParameters(final Instant fromDate, final Instant toDate, //
                                                  Instrument instrument)
    {
        Objects.requireNonNull(fromDate, "From date cannot be null");
        Objects.requireNonNull(toDate, "ToDate date cannot be null");
        Objects.requireNonNull(instrument, "Instrument date cannot be null");
    }

    private GenerateParameterValidator()
    {

    }
}
