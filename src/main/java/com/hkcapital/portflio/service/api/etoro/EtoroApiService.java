package com.hkcapital.portflio.service.api.etoro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkcapital.portflio.broker.etoro.JSON;
import com.hkcapital.portflio.broker.etoro.dto.order.EtoroLimitOrderDto;
import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portflio.broker.etoro.dto.order.EtoroOrderDetailsResponseDTO;
import com.hkcapital.portflio.broker.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portflio.service.registry.Service;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public interface EtoroApiService extends Service
{
    HttpResponse<String> createOrder(JSON order, String url) throws UnirestException;

    String getOrderInformation(Long orderId);

    EtoroOrderDetailsResponseDTO createLimitOrder(EtoroLimitOrderDto etoroLimitOrderDto) throws JsonProcessingException;

    EtoroPortfolioResponseDTO etoroPortfolio();

    EtoroOrderDetailsResponseDTO createMarketOrder(EtoroMarketOrderDto etoroMarketOrderDto) throws JsonProcessingException;

    List<Long> getOpenPositions(EtoroMarketOrderDto etoroMarketOrderDto, //
                                EtoroPortfolioResponseDTO portfolioResponseDTO);
}
