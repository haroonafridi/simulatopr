package com.hkcapital.portoflio.etoro.apiinformation;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class EtoroAPIInformationProdServiceImpl implements EtoroAPIInformationService
{


    private static final String X_REQUEST_ID = "x-request-id";
    private static final String X_API_KEY = "x-api-key";
    private static final String X_USER_KEY = "x-user-key";
    private static final String API_KEY = "sdgdskldFPLGfjHn1421dgnlxdGTbngdflg6290bRjslfihsjhSDsdgGHH25hjf";
    private static final String USER_KEY = "eyJjaSI6IjYwY2FiYjBiLTU1OTctNDQ4NS04ZjYzLTdlOWUwNTZlMGJiOCIsImVhbiI6IlVucmVnaXN0ZXJlZEFwcGxpY2F0aW9uIiwiZWsiOiJTMFhjNW1XUjkxVjhxTGlCTFVSVnE5cXQ1YVNraEZwZjVhY0FHWk90aFhad29ROE1NNlEyYTZUa3E0NFhQc1NSbnpXbFh1MzhMQ1JiTW8xYVEtbmloSDNxLUV0TTdOUG5WSTUtMWtNUlpxVV8ifQ__";
    private static final String CONTENT = "application/json";
    private static final String LIMIT_ORDER = "https://public-api.etoro.com/api/v1/trading/execution/limit-orders";
    private static final String MARKET_ORDER = "https://public-api.etoro.com/api/v1/trading/execution/market-open-orders/by-amount";
    private static final String INSTRUMENT_CANDLE_DATA = "https://public-api.etoro.com/api/v1/market-data/instruments/";
    private static final String ORDER_INFORMATION = "https://public-api.etoro.com/api/v1/trading/info/real/orders/";
    private static final String PORTFOLIO_INFORMATION = "https://public-api.etoro.com/api/v1/trading/info/portfolio";

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
        return "Profile EtoroAPIInformationProdServiceImpl{} activated!";
    }
}
