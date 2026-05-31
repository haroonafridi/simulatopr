package com.hkcapital.portflio.broker.etoro.server;

import com.hkcapital.portflio.broker.etoro.dto.order.EtoroMarketOrderDto;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/etoro")
@Profile("test")
public class EtoroSaveMarketOrderControllers
{
    private final EtoroOrdersCache ordersCache = new EtoroOrdersCacheImpl();
    @PostMapping("/saveOrder")
    public ResponseEntity<String> saveOrder(
            @RequestBody EtoroMarketOrderDto dto,
            @RequestHeader Map<String, String> headers)
    {
        return ResponseEntity.ok(ordersCache.createOrder(dto));
    }


    @GetMapping("/portfolio")
    public ResponseEntity<String> portfolio() throws IOException
    {
        String data = Files.readString(Path.of("D:\\portfolio-pnl-simulator\\src\\test\\resources\\data\\portfolio\\portfolio_0_gold_open_orders.json"));
        System.out.println("Portfolio data : " + data);
        return ResponseEntity.ok(data);
    }


}
