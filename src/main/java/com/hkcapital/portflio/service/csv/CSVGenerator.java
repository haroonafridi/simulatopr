package com.hkcapital.portflio.service.csv;

import java.time.Instant;

public interface CSVGenerator
{
    void generate(Instant start, Instant end);
}
