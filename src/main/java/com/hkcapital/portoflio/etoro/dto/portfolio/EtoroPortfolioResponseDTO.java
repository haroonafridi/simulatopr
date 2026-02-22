package com.hkcapital.portoflio.etoro.dto.portfolio;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EtoroPortfolioResponseDTO
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