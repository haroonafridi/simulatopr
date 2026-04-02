package com.hkcapital.portoflio.service.impl.etoro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hkcapital.portoflio.etoro.JSON;
import com.hkcapital.portoflio.etoro.dto.order.EtoroLimitOrderDto;
import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetailsResponseDTO;
import com.hkcapital.portoflio.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.List;

public interface EtoroApiService
{
    HttpResponse<String> createOrder(JSON order, String url) throws UnirestException;

    String getOrderInformation(Long orderId);

    EtoroOrderDetailsResponseDTO createLimitOrder(EtoroLimitOrderDto etoroLimitOrderDto) throws JsonProcessingException;

    EtoroPortfolioResponseDTO etoroPortfolio();

    EtoroOrderDetailsResponseDTO createMarketOrder(EtoroMarketOrderDto etoroMarketOrderDto) throws JsonProcessingException;

    List<Long> getOpenPositions(EtoroMarketOrderDto etoroMarketOrderDto, //
                                EtoroPortfolioResponseDTO portfolioResponseDTO);
}
