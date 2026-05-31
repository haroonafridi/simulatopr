package com.hkcapital.portflio.broker.etoro.server;

import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EtoroOrdersCacheImpl implements EtoroOrdersCache
{
    private final Map<String, EtoroMarketOrderDto> portfolio = new HashMap<>();
    @Override
    public String createOrder(EtoroMarketOrderDto etoroMarketOrderDto)
    {
        String orderId = UUID.randomUUID().toString().concat("_"+etoroMarketOrderDto.getInstrumentId());
        portfolio.put(orderId,etoroMarketOrderDto);
        return  orderId;
    }

    @Override
    public void closeOrder(String orderId)
    {
        portfolio.remove(orderId);
    }

    @Override
    public List<EtoroMarketOrderDto> portfolio()
    {
        return null;
    }
}
