package com.hkcapital.portoflio.service;

import com.hkcapital.portoflio.etoro.dto.order.EtoroLimitOrderDto;
import com.hkcapital.portoflio.etoro.dto.order.EtoroMarketOrderDto;
import com.hkcapital.portoflio.etoro.dto.order.EtoroOrderDetails;
import com.hkcapital.portoflio.etoro.dto.portfolio.EtoroPortfolioResponseDTO;
import com.hkcapital.portoflio.order.EtoroOrder;

public interface OrderManagerService extends Service
{
    String createMarketOrder(EtoroMarketOrderDto etoroMarketOrderDto);

    String createLimitOrder(EtoroLimitOrderDto etoroLimitOrderDto);

    String getOrderInformation(Long orderId);

    EtoroOrder createAndSaveMarketOrder(final EtoroMarketOrderDto etoroMarketOrderDto);

    EtoroPortfolioResponseDTO etoroPortfolio();

    EtoroOrder saveOrder(EtoroMarketOrderDto etoroMarketOrderDto,
                                EtoroOrderDetails orderDetails,
                                String etoroOrderToken);
}
