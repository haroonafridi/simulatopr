package com.hkcapital.portoflio.etoro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@Import(ObjectMapper.class)
public abstract class EtoroAbstractTest
{
    protected String expectedText = "{\"messages\":[{";
    protected ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public InputStream loadResource(String path)
    {
        return getClass().getResourceAsStream(path);
    }

    public EtoroPortfolioResponseDTO loadPortfolio(String path) throws IOException
    {
        return objectMapper.readValue(new String(loadResource(path).readAllBytes(), //
                        StandardCharsets.UTF_8),
                EtoroPortfolioResponseDTO.class);
    }
}
