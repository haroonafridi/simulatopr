package com.hkcapital.portoflio.config;

import com.hkcapital.portoflio.service.Service;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "etoro")
public class EtoroApiConfiguration implements Service
{
    private String apiKey;
    private String userKey;
    private String limitOrderUrl;
    private String marketOrderUrl;
    private String instrumentCandleDataUrl;
    private String orderInformationUrl;
    private String portfolioInformationUrl;

    public String getApiKey()
    {
        return apiKey;
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public String getUserKey()
    {
        return userKey;
    }

    public void setUserKey(String userKey)
    {
        this.userKey = userKey;
    }

    public String getLimitOrderUrl()
    {
        return limitOrderUrl;
    }

    public void setLimitOrderUrl(String limitOrderUrl)
    {
        this.limitOrderUrl = limitOrderUrl;
    }

    public String getMarketOrderUrl()
    {
        return marketOrderUrl;
    }

    public void setMarketOrderUrl(String marketOrderUrl)
    {
        this.marketOrderUrl = marketOrderUrl;
    }

    public String getInstrumentCandleDataUrl()
    {
        return instrumentCandleDataUrl;
    }

    public void setInstrumentCandleDataUrl(String instrumentCandleDataUrl)
    {
        this.instrumentCandleDataUrl = instrumentCandleDataUrl;
    }

    public String getOrderInformationUrl()
    {
        return orderInformationUrl;
    }

    public void setOrderInformationUrl(String orderInformationUrl)
    {
        this.orderInformationUrl = orderInformationUrl;
    }

    public String getPortfolioInformationUrl()
    {
        return portfolioInformationUrl;
    }

    public void setPortfolioInformationUrl(String portfolioInformationUrl)
    {
        this.portfolioInformationUrl = portfolioInformationUrl;
    }

    public String getXRequestId() {
        return "x-request-id";
    }

    public String getXApiKey() {
        return "x-api-key";
    }

    public String getXUserKey() {
        return "x-user-key";
    }
}
