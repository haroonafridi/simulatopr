package com.hkcapital.portflio.service.export.file.csv;

import com.hkcapital.portflio.config.DataPathConfig;
import com.hkcapital.portflio.service.export.ExtensionTypes;
import com.hkcapital.portflio.service.export.file.FileGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class CSVFileGenerator implements FileGenerator
{
    private DataPathConfig dataPathConfig;

    public CSVFileGenerator(DataPathConfig dataPathConfig)
    {
        this.dataPathConfig = dataPathConfig;
    }

    @Override
    public void fileOf(String data, String fileName, String type)
    {
        try
        {
            Files.writeString(Files.createDirectories(Path.of(dataPathConfig.getCsv() +type+"/")) //
                            .resolve(fileName + ExtensionTypes.csv.getType()),
                    data, StandardCharsets.UTF_8);

        } catch (IOException e)
        {
            log.error("Cannot create file with name due to an error = " + e.getMessage());
        }
    }
}
