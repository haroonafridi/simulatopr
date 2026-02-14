package com.hkcapital.portoflio.etoro.managers;

import com.hkcapital.portoflio.OrderManager;
import com.hkcapital.portoflio.etoro.JSON;
import com.hkcapital.portoflio.etoro.dto.EtoroLimitOrderDto;
import com.hkcapital.portoflio.etoro.dto.EtoroMarketOrderDto;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static com.hkcapital.portoflio.etoro.apiinformation.EtoroAPIInformation.*;

public class EtoroOrderManager implements OrderManager
{
    private static final Logger logger = LoggerFactory.getLogger(EtoroOrderManager.class);
    @Override
    public void createMarketOrder(EtoroMarketOrderDto etoroMarketOrderDto)
    {
        try
        {
            HttpResponse<String> response = createOrder(etoroMarketOrderDto, MARKET_ORDER);
            System.out.println(response.getBody().toString());
            logger.info("Market Order details ", response.getBody().toString());
        } catch (UnirestException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createLimitOrder(EtoroLimitOrderDto etoroLimitOrderDto)
    {
        try
        {
            HttpResponse<String> response = createOrder(etoroLimitOrderDto, LIMIT_ORDER);
            System.out.println(response.getBody().toString());
            logger.info("Limit Order details", response.getBody().toString());
        } catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws UnirestException
    {
        EtoroOrderManager e = new EtoroOrderManager();
        e.createMarketOrder(EtoroMarketOrderDto.createDummyOrder());
        e.createLimitOrder(EtoroLimitOrderDto.createDummyOrder());
        System.out.println("order infor = > "+e.getOrderInformation(328070114));
    }
    public HttpResponse<String> createOrder(JSON order, String url) throws UnirestException
    {
        HttpResponse<String> response = Unirest.post(url)
                .header("x-request-id", UUID.randomUUID().toString())
                .header("x-api-key", API_KEY)
                .header("x-user-key", USER_KEY)
                .header("Content-Type", "application/json")
                .body(order.toJson())
                .asString();

        return response;
    }

    public String getOrderInformation(Integer orderId) throws UnirestException
    {
        return Unirest.get( ORDER_INFORMATION.concat("/"+orderId))
                .header("x-request-id", UUID.randomUUID().toString())
                .header("x-api-key", API_KEY)
                .header("x-user-key", USER_KEY)
                .header("Content-Type", "application/json").asString().getBody().toString();

    }




}
