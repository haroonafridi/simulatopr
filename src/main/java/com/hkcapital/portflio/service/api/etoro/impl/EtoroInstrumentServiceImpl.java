package com.hkcapital.portflio.service.api.etoro.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.broker.etoro.instrument.EtoroInstrument;
import com.hkcapital.portflio.broker.etoro.instrument.InstrumentResponse;
import com.hkcapital.portflio.service.api.etoro.EtoroInstrumentService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EtoroInstrumentServiceImpl implements EtoroInstrumentService
{
    private final EtoroApiConfiguration etoroApiConfiguration;
    private final ObjectMapper objectMapper;

    public EtoroInstrumentServiceImpl(EtoroApiConfiguration etoroApiConfiguration, ObjectMapper objectMapper)
    {
        this.etoroApiConfiguration = etoroApiConfiguration;
        this.objectMapper = objectMapper;
    }

    @Override
    public InstrumentResponse fetchInstrumentInstrumentResponse(String ticker)
    {

        String url = etoroApiConfiguration.getInstrumentInformationUrl() + ticker;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-api-key", etoroApiConfiguration.getApiKey())
                .header("x-user-key", etoroApiConfiguration.getUserKey())
                .header("x-request-id", UUID.randomUUID().toString())
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        InstrumentResponse response = null;
        try
        {

            response =
                    objectMapper.readValue(client.send(request, HttpResponse.BodyHandlers.ofString()).body(),
                            InstrumentResponse.class);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        return response;
    }

    @Override
    public EtoroInstrument fetchInstrumentDetails(String ticker)
    {
        InstrumentResponse instResponse = fetchInstrumentInstrumentResponse(ticker);

        return instResponse.getItems().stream().filter(inst -> "Nasdaq".equals(inst.getInternalExchangeName()) &&
                        "Stocks".equals(inst.getInternalAssetClassName())) //
                .collect(Collectors.toList()) //
                .stream() //
                .findFirst() //
                .get();

    }

    @Override
    public Integer fetchInstrumentId(String ticker)
    {
        EtoroInstrument etoroInst = fetchInstrumentDetails(ticker);

        if (etoroInst != null) //
        {
            return etoroInst.getInternalInstrumentId();
        }
        return null;
    }


}
