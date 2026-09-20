package com.hkcapital.portflio.service.importexport;

import com.hkcapital.portflio.config.DataPathConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Slf4j
public class JSONFileGenerator extends FileGeneratorAbstract implements FileGenerator<String>
{
    public JSONFileGenerator(final DataPathConfig dataPathConfig)
    {
        super(dataPathConfig);
    }

    @Override
    public void fileOf(String data, String fileName, FileTypes type)
    {
        try
        {
            Files.writeString(Files.createDirectories(Path.of(dataPathConfig.getJson() + type.getType() + "/")) //
                            .resolve(fileName + ExtensionTypes.json.getType()),
                    data, StandardCharsets.UTF_8);

        } catch (IOException e)
        {
            log.error("Cannot create file with name due to an error = " + e.getMessage());
        }
    }
}
