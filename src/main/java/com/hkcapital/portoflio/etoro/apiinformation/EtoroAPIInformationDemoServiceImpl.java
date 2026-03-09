package com.hkcapital.portoflio.etoro.apiinformation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
public class EtoroAPIInformationDemoServiceImpl implements EtoroAPIInformationService
{
    private Logger logger = LoggerFactory.getLogger(EtoroAPIInformationDemoServiceImpl.class);
    public static final String X_REQUEST_ID = "x-request-id";
    public static final String X_API_KEY = "x-api-key";
    public static final String X_USER_KEY = "x-user-key";
    public static final String API_KEY = "sdgdskldFPLGfjHn1421dgnlxdGTbngdflg6290bRjslfihsjhSDsdgGHH25hjf";
    public static final String USER_KEY = "eyJjaSI6IjYwY2FiYjBiLTU1OTctNDQ4NS04ZjYzLTdlOWUwNTZlMGJiOCIsImVhbiI6IlVucmVnaXN0ZXJlZEFwcGxpY2F0aW9uIiwiZWsiOiJQSGVOS0kzeklPUDhpenNyeW1qNnhrV1JoVFNuVkJld0Z0RkdvSTJWMmRFR2pKNXAuYWV5WW9nUDNvTzVqQ0hkTFVZSUx2N0pRLmdIUTh0cTZXeldLYkJGaGNSclIyTGl0emF4UnJ3WDhNQV8ifQ__";
    public static final String CONTENT = "application/json";
    public static final String LIMIT_ORDER = "https://public-api.etoro.com/api/v1/trading/execution/demo/limit-orders";
    public static final String MARKET_ORDER = "https://public-api.etoro.com/api/v1/trading/execution/market-open-orders/by-amount";
    public static final String INSTRUMENT_CANDLE_DATA = "https://public-api.etoro.com/api/v1/market-data/instruments/";
    public static final String ORDER_INFORMATION = "https://public-api.etoro.com/api/v1/trading/info/demo/orders/";
    public static final String PORTFOLIO_INFORMATION = "https://public-api.etoro.com/api/v1/trading/info/demo/portfolio";


    public EtoroAPIInformationDemoServiceImpl() {
        logger.info("Development profile activated!!!");
    }

    @Override
    public String getXRequestId()
    {
        return X_REQUEST_ID;
    }

    @Override
    public String getXApIKey()
    {
        return X_API_KEY;
    }

    @Override
    public String getXUserKey()
    {
        return X_USER_KEY;
    }

    @Override
    public String getApiKey()
    {
        return API_KEY;
    }

    @Override
    public String getUserKey()
    {
        return USER_KEY;
    }

    @Override
    public String getContent()
    {
        return CONTENT;
    }

    @Override
    public String getLimitOrder()
    {
        return LIMIT_ORDER;
    }

    @Override
    public String getMarketOrder()
    {
        return MARKET_ORDER;
    }

    @Override
    public String getInstrumentCandle()
    {
        return INSTRUMENT_CANDLE_DATA;
    }

    @Override
    public String getOrderInformation()
    {
        return ORDER_INFORMATION;
    }

    @Override
    public String getPortfolioInformation()
    {
        return PORTFOLIO_INFORMATION;
    }

    @Override
    public String toString()
    {
        return "Profile EtoroAPIInformationDemoServiceImpl activated!";
    }
}
