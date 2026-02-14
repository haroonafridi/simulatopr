package com.hkcapital.portoflio;

import com.hkcapital.portoflio.etoro.dto.EtoroLimitOrderDto;
import com.hkcapital.portoflio.etoro.dto.EtoroMarketOrderDto;

public interface OrderManager
{
    void createMarketOrder(EtoroMarketOrderDto etoroMarketOrderDto);

    void createLimitOrder(EtoroLimitOrderDto etoroLimitOrderDto);

}
