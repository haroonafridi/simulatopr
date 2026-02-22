package com.hkcapital.portoflio.etoro.apiinformation;
import org.springframework.stereotype.Service;

@Service
public class EtoroAPIInformationProdServiceImpl implements EtoroAPIInformationService
{
    public static final String X_REQUEST_ID = "x-request-id";
    public static final String X_API_KEY = "x-api-key";
    public static final String X_USER_KEY = "x-user-key";
    public static final String API_KEY = "sdgdskldFPLGfjHn1421dgnlxdGTbngdflg6290bRjslfihsjhSDsdgGHH25hjf";
    public static final String USER_KEY_PROD = "eyJjaSI6IjYwY2FiYjBiLTU1OTctNDQ4NS04ZjYzLTdlOWUwNTZlMGJiOCIsImVhbiI6IlVucmVnaXN0ZXJlZEFwcGxpY2F0aW9uIiwiZWsiOiI3Qklqekp6a0lvamtYb2ZrTlJ5a21MSE1FSTUta0hKU2dFYklyaW56MjZ5QjIyZTdzUkFEUllkWHlQbDR4TGxRZXJhYlA3aHZ5NndXeHBBN0pmY2NiamJURTdEclh4WFVOWlhyR3ZmaEVkRV8ifQ__";
    public static final String CONTENT = "application/json";
    public static final String LIMIT_ORDER = "https://public-api.etoro.com/api/v1/trading/execution/demo/limit-orders";
    public static final String MARKET_ORDER = "https://public-api.etoro.com/api/v1/trading/execution/market-open-orders/by-amount";
    public static final String INSTRUMENT_CANDLE_DATA = "https://public-api.etoro.com/api/v1/market-data/instruments/";
    public static final String ORDER_INFORMATION = "https://public-api.etoro.com/api/v1/trading/info/demo/orders/";
    public static final String PORTFOLIO_INFORMATION = "https://public-api.etoro.com/api/v1/trading/info/demo/portfolio";

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
        return USER_KEY_PROD;
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
