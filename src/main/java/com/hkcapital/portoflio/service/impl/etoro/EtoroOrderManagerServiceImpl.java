package com.hkcapital.portoflio.service.impl.etoro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portoflio.config.EtoroApiConfiguration;
import com.hkcapital.portoflio.etoro.JSON;
import com.hkcapital.portoflio.etoro.dto.order.EtoroLimitOrderDto;
import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetailsResponseDTO;
import com.hkcapital.portoflio.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portoflio.order.EtoroOrder;
import com.hkcapital.portoflio.repository.OrderRepository;
import com.hkcapital.portoflio.service.OrderManagerService;
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
    private final EtoroApiConfiguration etoroApiConfiguration;

    private final ObjectMapper objectMapper;

    public EtoroOrderManagerServiceImpl(final OrderRepository orderRepository, //
                                        final EtoroApiConfiguration etoroApiConfiguration,
                                        final ObjectMapper objectMapper)
    {
        this.orderRepository = orderRepository;
        this.etoroApiConfiguration = etoroApiConfiguration;
        this.objectMapper = objectMapper;
    }

    @Override
    public String createMarketOrder(EtoroMarketOrderDto etoroMarketOrderDto)
    {
        try
        {
            logger.info("Fetching portfolio information");
            EtoroPortfolioResponseDTO portfolioResponseDTO = etoroPortfolio();
            List<Long> openPositions = getOpenPositions(etoroMarketOrderDto, portfolioResponseDTO);
            logger.info("Portfolio information successfully fetched: No of open positions found {}", openPositions.size());
            if (openPositions.size() > 0)
            {
                logger.info("Cannot open order, orders for instrument {} found", etoroMarketOrderDto.getInstrumentId(), openPositions);
                return null;
            }

            logger.info("Creating market Order [{}]", etoroMarketOrderDto.toJson());
            HttpResponse<String> response = createOrder(etoroMarketOrderDto, etoroApiConfiguration.getMarketOrderUrl());
            if (response != null)
            {
                logger.info("Market Order in etoro [ {} ]", response.getBody());
                return response.getBody().toString();
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

    private static List<Long> getOpenPositions(EtoroMarketOrderDto etoroMarketOrderDto, EtoroPortfolioResponseDTO portfolioResponseDTO)
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
    public String createLimitOrder(EtoroLimitOrderDto etoroLimitOrderDto)
    {
        try
        {
            logger.info("Creating limit Order [{}]", etoroLimitOrderDto.toJson());
            HttpResponse<String> response = createOrder(etoroLimitOrderDto, etoroApiConfiguration.getLimitOrderUrl());
            logger.info("Limit Order in etoro [{}]", response.getBody());
            return response.getBody().toString();
        } catch (UnirestException e)
        {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> createOrder(JSON order, String url) throws UnirestException
    {

        System.out.println("url "+url);
        System.out.println("api key "+etoroApiConfiguration.getApiKey());
        System.out.println("user key "+etoroApiConfiguration.getUserKey());

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
    public EtoroOrder createAndSaveMarketOrder(EtoroMarketOrderDto etoroMarketOrderDto)
    {
        final String order = createMarketOrder(etoroMarketOrderDto);
        if (order != null)
        {
            try
            {
                EtoroOrderDetailsResponseDTO orderResponse = objectMapper.readValue(order, EtoroOrderDetailsResponseDTO.class);
                EtoroOrderDetails orderDetails = orderResponse.getOrderForOpen();
                return saveOrder(etoroMarketOrderDto,orderDetails,orderResponse.getToken());
            } catch (JsonProcessingException e)
            {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public EtoroOrder saveOrder(EtoroMarketOrderDto etoroMarketOrderDto,
                                EtoroOrderDetails orderDetails,
                                String etoroOrderToken)
    {
        EtoroOrder etoroOrder = new EtoroOrder();
        etoroOrder.setStatus("SENT");
        etoroOrder.setOderType(etoroMarketOrderDto.getOrderType());
        etoroOrder.fill(orderDetails);
        etoroOrder.setTokenId(etoroOrderToken);
        etoroOrder.setBid(etoroMarketOrderDto.getBid());
        etoroOrder.setAsk(etoroMarketOrderDto.getAsk());
        etoroOrder.setMaxAllowedSlippage(etoroMarketOrderDto.getMaxAllowedSlippage());
        etoroOrder.setEtoroSlippage(etoroMarketOrderDto.getEtoroSlippage());
        orderRepository.save(etoroOrder);
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
                System.out.println("url = "+etoroApiConfiguration.getPortfolioInformationUrl());
                System.out.println("api key = "+ etoroApiConfiguration.getApiKey());
                System.out.println("user key = "+  etoroApiConfiguration.getUserKey());
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
