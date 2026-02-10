package com.hkcapital.portoflio.etoro;

import com.hkcapital.portoflio.OrderManager;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.UUID;

import static com.hkcapital.portoflio.etoro.EtoroAPIInformation.*;

public class EtoroOrderManager implements OrderManager
{


    public static final String jsonBody = "{ \"InstrumentID\": 207010,\n \"IsBuy\": true,\n \"Leverage\": 10,\n \"Amount\": 1000,\n \"StopLossRate\": 100,\n \"TakeProfitRate\": 100,\n \"IsTslEnabled\": false,\n \"IsNoStopLoss\": true,\n \"IsNoTakeProfit\": true\n}";

    @Override
    public void createMarketOrder()
    {
        try {
//            HttpResponse<String> response = Unirest.post("https://public-api.etoro.com/api/v1/trading/execution/demo/market-open-orders/by-units")
//                    .header("x-request-id", UUID.randomUUID().toString())
//                    .header("x-api-key", API_KEY)
//                    .header("x-user-key", USER_KEY)
//                    .header("Content-Type", "application/json")
//                    .body("{\n  \"InstrumentID\": 207010,\n  \"IsBuy\": true,\n  \"Leverage\": 1 ,\n  \"AmountInUnits\": 2,\n  \"StopLossRate\": 20,\n  \"TakeProfitRate\": 20,\n  \"IsTslEnabled\": false,\n  \"IsNoStopLoss\": true,\n  \"IsNoTakeProfit\": true\n}")
//                    .asString();
//
//            System.out.println(response.getBody());

            HttpResponse<String> auditResponse = Unirest.get("https://public-api.etoro.com/api/v1/trading/info/demo/orders/326341388")
                    .header("x-request-id", UUID.randomUUID().toString())
                    .header("x-api-key", API_KEY)
                    .header("x-user-key", USER_KEY)
                    .asString();
            System.out.println("Backend Status: " + auditResponse.getBody());


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
            String jsonBody = "{"
                    + "\"InstrumentID\": 219000,"
                    + "\"IsBuy\": true,"
                    + "\"Leverage\": 1,"
                    + "\"Amount\": 500,"
                    + "\"StopLossRate\": 60000,"
                    + "\"TakeProfitRate\": 85000,"
                    + "\"Rate\": 69000,"
                    + "\"IsTslEnabled\": false,"
                    + "\"CID\": 12345678"
                    + "}";



            HttpResponse<String> response = Unirest.post(ETORO_LIMIT_ORDER)
                    .header("x-request-id", UUID.randomUUID().toString())
                    .header("x-api-key", API_KEY)
                    .header("x-user-key", USER_KEY)
                    .header("Content-Type", "application/json") // Ensure this matches your CONTENT variable
                    .body(jsonBody)
                    .asString();

            System.out.println("Response => "+response.getBody());
        } catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        EtoroOrderManager e = new EtoroOrderManager();
        e.createMarketOrder();
       // e.createLimitOrder();
    }
}
