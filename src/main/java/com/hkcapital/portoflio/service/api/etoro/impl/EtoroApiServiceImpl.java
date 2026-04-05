package com.hkcapital.portoflio.service.api.etoro.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.broker.etoro.JSON;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroLimitOrderDto;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.broker.etoro.dto.order.EtoroOrderDetailsResponseDTO;
import com.hkcapital.portoflio.broker.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portoflio.values.order.OrderTypes;
import com.hkcapital.portoflio.service.api.etoro.EtoroApiService;
import com.hkcapital.portoflio.service.orders.impl.etoro.EtoroOrderManagerServiceImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EtoroApiServiceImpl implements EtoroApiService
{
    private static final Logger logger = LoggerFactory.getLogger(EtoroOrderManagerServiceImpl.class);
    private final EtoroApiConfiguration etoroApiConfiguration;
    private final ObjectMapper objectMapper;
    public EtoroApiServiceImpl(EtoroApiConfiguration etoroApiConfiguration, ObjectMapper objectMapper)
    {
        this.etoroApiConfiguration = etoroApiConfiguration;
        this.objectMapper = objectMapper;
    }
    @Override
    public HttpResponse<String> createOrder(JSON order, String url) throws UnirestException
    {
        HttpResponse<String> response = Unirest.post(url)
                .header(etoroApiConfiguration.getXRequestId(), UUID.randomUUID().toString())//
                .header(etoroApiConfiguration.getXApiKey(), etoroApiConfiguration.getApiKey())//
                .header(etoroApiConfiguration.getXUserKey(), etoroApiConfiguration.getUserKey())//
                .header("Content-Type", "application/json")//
                .body(order.toJson())//
                .asString();
        return response;
    }

    @Override
    public String getOrderInformation(Long orderId)
    {
        try
        {
            HttpResponse<String> response = Unirest.get(etoroApiConfiguration.getOrderInformationUrl().concat(orderId.toString()))//
                    .header("x-request-id", UUID.randomUUID().toString())//
                    .header("x-api-key", etoroApiConfiguration.getApiKey())//
                    .header("x-user-key", etoroApiConfiguration.getUserKey())//
                    .asString();
            logger.info("Order details [{}]", orderId, response.getBody());
            return response.getBody();
        } catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EtoroOrderDetailsResponseDTO createLimitOrder(EtoroLimitOrderDto etoroLimitOrderDto) throws JsonProcessingException
    {
        try
        {
            logger.info("Creating limit Order [{}]", etoroLimitOrderDto.toJson());
            HttpResponse<String> response = createOrder(etoroLimitOrderDto, etoroApiConfiguration.getLimitOrderUrl());
            logger.info("Limit Order in etoro [{}]", response.getBody());
            return  objectMapper.readValue(response.getBody(), EtoroOrderDetailsResponseDTO.class);
        } catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EtoroOrderDetailsResponseDTO createMarketOrder(EtoroMarketOrderDto etoroMarketOrderDto) throws JsonProcessingException
    {
        try
        {
            logger.info("Fetching portfolio information");
            EtoroPortfolioResponseDTO portfolioResponseDTO = etoroPortfolio();
            List<Long> openPositions = getOpenPositions(etoroMarketOrderDto, portfolioResponseDTO);
            logger.info("Portfolio information successfully fetched: No of open positions found {}", openPositions.size());
            if (openPositions.size() > 0 && OrderTypes.AUTO.getOrderType().equals(etoroMarketOrderDto.getOrderType()))
            {
                logger.info("Cannot open order, orders for instrument {} found", etoroMarketOrderDto.getInstrumentId(), openPositions);
                return null;
            }

            logger.info("Creating market Order [{}]", etoroMarketOrderDto.toJson());
            HttpResponse<String> response = createOrder(etoroMarketOrderDto, etoroApiConfiguration.getMarketOrderUrl());
            if (response != null)
            {
                logger.info("Market Order in etoro [ {} ]", response.getBody());
                return objectMapper.readValue(response.getBody(), EtoroOrderDetailsResponseDTO.class); // response.getBody().toString();
            }//
            else
            {
                logger.error("No order found exiting ");
                return null;
            }

        } catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Long> getOpenPositions(EtoroMarketOrderDto etoroMarketOrderDto, //
                                               EtoroPortfolioResponseDTO portfolioResponseDTO)
    {
        List<Long> openPositions = portfolioResponseDTO.getClientPortfolio()
                .getPositions().stream()
                .filter(p -> p.getInstrumentId().intValue()
                        == etoroMarketOrderDto.getInstrumentId())
                .collect(Collectors.toList())
                .stream().map(o -> o.getOrderId())
                .collect(Collectors.toList());
        return openPositions;
    }

    @Override
    public EtoroPortfolioResponseDTO etoroPortfolio()
    {
        HttpResponse<String> response = null;
        try
        {
            try
            {
                response = Unirest.get(etoroApiConfiguration.getPortfolioInformationUrl())//
                        .header(etoroApiConfiguration.getXRequestId(), UUID.randomUUID().toString())//
                        .header(etoroApiConfiguration.getXApiKey(), etoroApiConfiguration.getApiKey())//
                        .header(etoroApiConfiguration.getXUserKey(), etoroApiConfiguration.getUserKey()).asString();
                logger.info("portfolio response = [{}]", response.getBody());
            } catch (UnirestException e)
            {
                throw new RuntimeException(e);
            }
            return objectMapper.readValue(response.getBody(), EtoroPortfolioResponseDTO.class);

        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

}
