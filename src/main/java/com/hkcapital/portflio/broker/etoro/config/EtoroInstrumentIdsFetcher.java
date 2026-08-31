package com.hkcapital.portflio.broker.etoro.config;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class EtoroInstrumentIdsFetcher {

    public static void main(String[] args) throws Exception {

        String symbol = "TSLA";

        String url =
                "https://public-api.etoro.com/api/v1/market-data/search?internalSymbolFull="
                        + symbol;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-api-key", "sdgdskldFPLGfjHn1421dgnlxdGTbngdflg6290bRjslfihsjhSDsdgGHH25hjf")
                .header("x-user-key", "eyJjaSI6IjYwY2FiYjBiLTU1OTctNDQ4NS04ZjYzLTdlOWUwNTZlMGJiOCIsImVhbiI6IlVucmVnaXN0ZXJlZEFwcGxpY2F0aW9uIiwiZWsiOiJManAzOVFoNkM3cnNGeUh5ekd0NkpkMVMtcnN1cERzaUhLWS0xMUFwLldVVTJtRWlWMi03WHBuWUx6ZHdFQnMxUE1mSmo4ck91emhHNUZpRUZWOGNNakJWQjFORUtUWG5FSEtSTFBmeGJ1a18ifQ__")
                .header("x-request-id", UUID.randomUUID().toString())
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}