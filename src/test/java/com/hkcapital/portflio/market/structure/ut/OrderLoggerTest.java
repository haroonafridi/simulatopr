package com.hkcapital.portflio.market.structure.ut;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hkcapital.portflio.market.structure.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;

class OrderLoggerTest
{
    final Distance distance = Distance.builder().referencePrice(4700d).currentPrice(4701d).build();
    final Instant someDate = Instant.parse("2007-12-03T10:15:30.00Z");

    BandKey bandKey = new BandKey(BandType.HIGH, 4700, 4720);
    MarketPriceBand marketPriceBand = MarketPriceBand.builder()
            .bandKey(bandKey)
            .bandType(BandType.HIGH)
            .lowerBound(4700d)
            .upperBound(4720d)
            .marketVisitCount(1)
            .build();
    final OrderLogger orderLogger = OrderLogger.builder()//
            .orderCount(1)//
            .orderType(OrderType.SELL)//
            .marketPriceBand(marketPriceBand)
            .distance(distance)//
            .price(4700d)//
            .absPoint(2d)//
            .pTBelow(3d)//
            .instant(someDate)//
            .build();

    @Test
    public void shouldCreateOrderLogger()
    {
        Assertions.assertNotNull(orderLogger);
        Assertions.assertAll("Should verify order logger ", () ->
        {
            Assertions.assertEquals(1, orderLogger.getOrderCount());
            Assertions.assertEquals(OrderType.SELL, orderLogger.getOrderType());
            Assertions.assertEquals(distance, orderLogger.getDistance());
            Assertions.assertEquals(4700d, orderLogger.getPrice());
            Assertions.assertEquals(2d, orderLogger.getAbsPoint());
            Assertions.assertEquals(3d, orderLogger.getPTBelow());
            Assertions.assertAll("Should verify market bands", () ->
            {
                Assertions.assertNotNull(orderLogger.getMarketPriceBand());
                Assertions.assertEquals(4700d, orderLogger.getMarketPriceBand().getLowerBound());
                Assertions.assertEquals(4720d, orderLogger.getMarketPriceBand().getUpperBound());
                Assertions.assertAll("Should market price bands key", () ->
                {
                    Assertions.assertNotNull(orderLogger.getMarketPriceBand().getBandKey());
                    Assertions.assertEquals(BandType.HIGH, orderLogger.getMarketPriceBand().getBandKey().bandType());
                    Assertions.assertEquals(4700d, orderLogger.getMarketPriceBand().getBandKey().lowerBound());
                    Assertions.assertEquals(4720d, orderLogger.getMarketPriceBand().getBandKey().upperBound());
                });
                Assertions.assertEquals(1, orderLogger.getMarketPriceBand().getMarketVisitCount());
            });
            Assertions.assertEquals(someDate, orderLogger.getInstant());
        });
    }

    @Test
    public void shouldCreateOrderLoggerJson() throws IOException
    {
        JsonFactory factory = JsonFactory.builder().build();
        ObjectMapper mapper = new ObjectMapper(factory);
        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        Assertions.assertEquals(getExpectedJson(),mapper.writeValueAsString(orderLogger).toString().trim());

    }


    private String getExpectedJson()
    {
        return "{\"marketPriceBand\":{\"bandType\":\"HIGH\",\"bandKey\":\"high_4700_4720\",\"lowerBound\":4700.0,\"upperBound\":4720.0,\"marketVisitCount\":1},\"distance\":{\"referencePrice\":4700.0,\"currentPrice\":4701.0,\"points\":1.0,\"percent\":0.02127659574468085,\"above\":true,\"below\":false},\"orderType\":\"SELL\",\"price\":4700.0,\"absPoint\":2.0,\"orderCount\":1,\"instant\":1196676930.000000000,\"ptbelow\":3.0,\"ptabove\":null}".trim();
    }
}