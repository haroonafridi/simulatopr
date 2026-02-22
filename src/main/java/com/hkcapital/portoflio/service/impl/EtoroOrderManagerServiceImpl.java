package com.hkcapital.portoflio.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hkcapital.portoflio.etoro.JSON;
import com.hkcapital.portoflio.etoro.apiinformation.EtoroAPIInformationService;
import com.hkcapital.portoflio.etoro.dto.order.EtoroLimitOrderDto;
import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetailsResponseDTO;
import com.hkcapital.portoflio.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portoflio.model.Position;
import com.hkcapital.portoflio.model.Strategy;
import com.hkcapital.portoflio.order.EtoroOrder;
import com.hkcapital.portoflio.repository.OrderRepository;
import com.hkcapital.portoflio.repository.ServiceRegistery;
import com.hkcapital.portoflio.service.OrderManagerService;
import com.hkcapital.portoflio.service.StrategyService;
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
public class EtoroOrderManagerServiceImpl implements OrderManagerService
{
    private static final Logger logger = LoggerFactory.getLogger(EtoroOrderManagerServiceImpl.class);
    private final OrderRepository orderRepository;
    private final EtoroAPIInformationService apiInformationService;

    public EtoroOrderManagerServiceImpl(final OrderRepository orderRepository, //
                                        final EtoroAPIInformationService apiInformationService)
    {
        this.orderRepository = orderRepository;
        this.apiInformationService = apiInformationService;
    }

    @Override
    public String createMarketOrder(EtoroMarketOrderDto etoroMarketOrderDto)
    {
        try
        {
            logger.info("Running profile = {}", apiInformationService);
            logger.info("Fetching portfolio information");
            EtoroPortfolioResponseDTO portfolioResponseDTO = etoroPortfolio();
            List<Long> openPositions = portfolioResponseDTO.getClientPortfolio()
                    .getPositions().stream()
                    .filter(p -> p.getInstrumentId().intValue()
                            == etoroMarketOrderDto.getInstrumentId())
                    .collect(Collectors.toList())
                    .stream().map(o -> o.getOrderId())
                    .collect(Collectors.toList());
            logger.info("Portfolio information successfully fetched: No of open positions found {}", openPositions.size());
            if (openPositions.size() > 0)
            {
                logger.info("Cannot open order, orders for instrument {} found", etoroMarketOrderDto.getInstrumentId(), openPositions);
                return null;
            }

            logger.info("Creating market Order [{}]", etoroMarketOrderDto.toJson());
            HttpResponse<String> response = createOrder(etoroMarketOrderDto, apiInformationService.getMarketOrder());
            if (response != null)
            {
                logger.info("Market Order in etoro [ {} ]", response.getBody());
                return response.getBody().toString();
            } else
            {
                logger.error("No order found exiting ");
                return null;
            }

        } catch (UnirestException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createLimitOrder(EtoroLimitOrderDto etoroLimitOrderDto)
    {
        try
        {
            logger.info("Creating limit Order [{}]", etoroLimitOrderDto.toJson());
            HttpResponse<String> response = createOrder(etoroLimitOrderDto, apiInformationService.getLimitOrder());
            logger.info("Limit Order in etoro [{}]", response.getBody());
            return response.getBody().toString();
        } catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> createOrder(JSON order, String url) throws UnirestException
    {

        HttpResponse<String> response = Unirest.post(url)
                .header("x-request-id", UUID.randomUUID().toString())//
                .header("x-api-key", apiInformationService.getApiKey())//
                .header("x-user-key", apiInformationService.getUserKey())//
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
            HttpResponse<String> response = Unirest.get(apiInformationService.getOrderInformation().concat(orderId.toString()))//
                    .header("x-request-id", UUID.randomUUID().toString())//
                    .header("x-api-key", apiInformationService.getApiKey())//
                    .header("x-user-key", apiInformationService.getUserKey())//
                    .asString();
            logger.info("Order details [{}]", orderId, response.getBody());
            return response.getBody();
        } catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EtoroOrder createAndSaveMarketOrder(EtoroMarketOrderDto etoroMarketOrderDto)
    {

        EtoroOrder etoroOrder = new EtoroOrder();
        final String order = createMarketOrder(etoroMarketOrderDto);
        if (order != null)
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            try
            {
                EtoroOrderDetailsResponseDTO orderResponse = mapper.readValue(order, EtoroOrderDetailsResponseDTO.class);
                EtoroOrderDetails orderDetails = orderResponse.getOrderForOpen();
                etoroOrder.fill(orderDetails);
                etoroOrder.setStatus("SENT");
                etoroOrder.setTokenId(orderResponse.getToken());
                orderRepository.save(etoroOrder);
            } catch (JsonProcessingException e)
            {
                throw new RuntimeException(e);
            }
        }
        return etoroOrder;
    }

    @Override
    public EtoroPortfolioResponseDTO etoroPortfolio()
    {
        HttpResponse<String> response = null;
        try
        {
            try
            {
                response = Unirest.get(apiInformationService.getPortfolioInformation())//
                        .header(apiInformationService.getXRequestId(), UUID.randomUUID().toString())//
                        .header(apiInformationService.getXApIKey(), apiInformationService.getApiKey())//
                        .header(apiInformationService.getXUserKey(), apiInformationService.getUserKey()).asString();
                logger.info("portfolio response = [{}]", response.getBody());
            } catch (UnirestException e)
            {
                throw new RuntimeException(e);
            }
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(response.getBody(), EtoroPortfolioResponseDTO.class);

        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }


}
