package com.hkcapital.portflio.service.importexport.export.file.csv;

import com.hkcapital.portflio.config.DataPathConfig;
import com.hkcapital.portflio.service.importexport.ExtensionTypes;
import com.hkcapital.portflio.service.importexport.FileGenerator;
import com.hkcapital.portflio.service.importexport.FileGeneratorAbstract;
import com.hkcapital.portflio.service.importexport.FileTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service("csvFileGenerator")
@Slf4j
public class CSVFileGenerator extends FileGeneratorAbstract implements FileGenerator<String>
{

    public CSVFileGenerator(final DataPathConfig dataPathConfig)
    {
        super(dataPathConfig);
    }

    @Override
    public void fileOf(String data, String fileName, FileTypes type)
    {
        try
        {
            Files.writeString(Files.createDirectories(Path.of(dataPathConfig.getCsv() + type.getType() + "/")) //
                            .resolve(fileName + ExtensionTypes.csv.getType()),
                    data, StandardCharsets.UTF_8);

        } catch (IOException e)
        {
            log.error("Cannot create file with name due to an error = " + e.getMessage());
        }
    }
}
