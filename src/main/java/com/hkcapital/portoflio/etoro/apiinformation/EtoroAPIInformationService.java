package com.hkcapital.portoflio.etoro.apiinformation;

import com.hkcapital.portoflio.service.Service;

public interface EtoroAPIInformationService extends Service
{

    String getXRequestId();

    String getXApIKey();

    String getXUserKey();

    String getApiKey();

    String getUserKey();

    String getContent();

    String getLimitOrder();

    String getMarketOrder();

    String getInstrumentCandle();

    String getOrderInformation();

    String getPortfolioInformation();


}
