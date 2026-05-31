package com.hkcapital.portflio.service.bandlogger.impl;

import com.hkcapital.portflio.model.BandLogger;
import com.hkcapital.portflio.repository.bandlogger.BandLoggerRepository;
import com.hkcapital.portflio.service.bandlogger.Bandlogger;
import org.springframework.stereotype.Service;

@Service
public class BandloggerImpl implements Bandlogger
{
    private final BandLoggerRepository bandLoggerRepository;

    public BandloggerImpl(BandLoggerRepository bandLoggerRepository)
    {
        this.bandLoggerRepository = bandLoggerRepository;
    }

    @Override
    public void save(BandLogger bandLogger)
    {
        bandLoggerRepository.save(bandLogger);
    }
}
