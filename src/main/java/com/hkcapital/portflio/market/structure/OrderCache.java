package com.hkcapital.portflio.market.structure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hkcapital.portflio.service.api.etoro.websocket.LiveInstrumentRate;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrderCache
{
    private final Logger logger = LoggerFactory.getLogger(OrderCache.class);

    @Getter
    private final Map<String, Order> ordersCache = new ConcurrentHashMap<>();

    public void register(String key, Order order)
    {
        ordersCache.put(key, order);
    }

    public void process(LiveInstrumentRate liveInstrumentRate, ObjectMapper objectMapper)
    {
        ordersCache.entrySet().forEach((e) ->
        {
            if (e.getValue().getStatus().equals("OPEN"))
            {
                Order order = e.getValue();
                if (order.getOrderType().equals(OrderType.SELL))
                {
                    if (liveInstrumentRate.getAsk() <= order.getTp())
                    {
                        order.setStatus("CLOSED");
                        order.setInfo("TP hit and Order closed at price [" + liveInstrumentRate.getAsk() + "]");
                    }

                    if (liveInstrumentRate.getAsk() >= order.getSl())
                    {
                        order.setStatus("CLOSED");
                        order.setInfo("SL hit and Order closed at price [" + liveInstrumentRate.getAsk() + "]");
                    }
                }

                if (order.getOrderType().equals(OrderType.BUY))
                {
                    if (liveInstrumentRate.getAsk() >= order.getTp())
                    {
                        order.setStatus("CLOSED");
                        order.setInfo("TP hit and Order closed at price [" + liveInstrumentRate.getAsk() + "]");
                    }

                    if (liveInstrumentRate.getAsk() <= order.getSl())
                    {
                        order.setStatus("CLOSED");
                        order.setInfo("SL hit and Order closed at price [" + liveInstrumentRate.getAsk() + "]");
                    }
                }
                e.setValue(order);
            }
        });

        try
        {
            if(!ordersCache.isEmpty())
            {
                logger.info("{}", objectMapper.writeValueAsString(ordersCache));
            }

        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
    }

}
