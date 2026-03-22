package com.hkcapital.portoflio.etoro.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(ObjectMapper.class)
public abstract class LiveFeedAbstractTest
{
    protected String expectedText = "{\"messages\":[{";
    protected ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
}
