package com.hkcapital.portflio.service.importexport;

import com.hkcapital.portflio.config.DataPathConfig;
import org.springframework.stereotype.Component;

@Component
public abstract class FileGeneratorAbstract
{
    protected final DataPathConfig dataPathConfig;

    public FileGeneratorAbstract(DataPathConfig dataPathConfig)
    {
        this.dataPathConfig = dataPathConfig;
    }
}
