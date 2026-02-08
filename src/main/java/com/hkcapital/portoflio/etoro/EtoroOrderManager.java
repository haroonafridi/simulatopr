package com.hkcapital.portoflio.etoro;

import com.hkcapital.portoflio.OrderManager;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.UUID;

import static com.hkcapital.portoflio.etoro.EtoroAPIInformation.*;

public class EtoroOrderManager implements OrderManager
{


    @Override
    public void createMarketOrder()
    {
        try
        {
            HttpResponse<String> response = Unirest.post(ETORO_LIMIT_ORDER)
                    .header("x-api-key", API_KEY)
                    .header("x-request-id", UUID.randomUUID().toString())
                    .header("x-user-key", USER_KEY)
                    .header("Content-Type", CONTENT)
                    .body("{\n  \"InstrumentID\": 219000,\n  \"IsBuy\": true,\n  \"Leverage\": 1,\n  \"Amount\": 50,\n  \"AmountInUnits\": null,\n  \"StopLossRate\": 20,\n  \"TakeProfitRate\": 50,\n  \"Rate\": 123,\n  \"IsTslEnabled\": false,\n  \"IsDiscounted\": false,\n  \"IsNoStopLoss\": false,\n  \"IsNoTakeProfit\": true,\n  \"CID\": 123\n}")
                    .asString();
            System.out.println("response => "+response.getBody());
        } catch (UnirestException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createLimitOrder()
    {
        try
        {
            HttpResponse<String> response = Unirest.post(ETORO_LIMIT_ORDER)
                    .header("x-request-id", UUID.randomUUID().toString())
                    .header("x-api-key", API_KEY)
                    .header("x-user-key", USER_KEY)
                    .header("Content-Type", CONTENT)
                    .body("{ InstrumentID : 219000 ,\n  \"IsBuy\": true,\n  \"Leverage\": 2,\n  \"Amount\": 500,\n  \"AmountInUnits\": null,\n  \"StopLossRate\": 69700,\n  \"TakeProfitRate\": 85000,\n  \"Rate\": 69000,\n  \"IsTslEnabled\": false,\n  \"IsDiscounted\": null,\n  \"IsNoStopLoss\": null,\n  \"IsNoTakeProfit\": null,\n  \"CID\": 786\n}")
                    .asString();

            System.out.println("Response => "+response.getBody());
        } catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        EtoroOrderManager e = new EtoroOrderManager();

       // e.createMarketOrder();

        e.createLimitOrder();
    }
}
