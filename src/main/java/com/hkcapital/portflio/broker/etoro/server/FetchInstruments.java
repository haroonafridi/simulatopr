package com.hkcapital.portflio.broker.etoro.server;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.UUID;

public class FetchInstruments
{
    public static void main(String[] args) throws UnirestException
    {
        HttpResponse<String> response = Unirest.get("https://public-api.etoro.com/api/v1/market-data/search?internalSymbolFull=NSDQ100")
                .header("x-request-id", UUID.randomUUID().toString())
                .header("x-api-key", "sdgdskldFPLGfjHn1421dgnlxdGTbngdflg6290bRjslfihsjhSDsdgGHH25hjf")
                .header("x-user-key", "eyJjaSI6IjYwY2FiYjBiLTU1OTctNDQ4NS04ZjYzLTdlOWUwNTZlMGJiOCIsImVhbiI6IlVucmVnaXN0ZXJlZEFwcGxpY2F0aW9uIiwiZWsiOiJTMFhjNW1XUjkxVjhxTGlCTFVSVnE5cXQ1YVNraEZwZjVhY0FHWk90aFhad29ROE1NNlEyYTZUa3E0NFhQc1NSbnpXbFh1MzhMQ1JiTW8xYVEtbmloSDNxLUV0TTdOUG5WSTUtMWtNUlpxVV8ifQ__")
                .asString();

        System.out.println(response.getBody());
    }
}
