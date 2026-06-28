package com.hkcapital.portflio.broker.etoro.dto.portfolio;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class EtoroPortfolioResponseDTO implements Serializable
{
    @JsonProperty("clientPortfolio")
    private EtoroClientPortfolioDTO clientPortfolio;

    public EtoroClientPortfolioDTO getClientPortfolio()
    {
        return clientPortfolio;
    }

    public void setClientPortfolio(EtoroClientPortfolioDTO clientPortfolio)
    {
        this.clientPortfolio = clientPortfolio;
    }
}