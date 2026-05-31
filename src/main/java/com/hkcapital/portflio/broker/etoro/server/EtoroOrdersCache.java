package com.hkcapital.portflio.broker.etoro.server;

import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;

import java.util.List;

public interface EtoroOrdersCache
{
    String createOrder(EtoroMarketOrderDto etoroMarketOrderDto);
    void closeOrder(String orderId);
    List<EtoroMarketOrderDto> portfolio();
}
