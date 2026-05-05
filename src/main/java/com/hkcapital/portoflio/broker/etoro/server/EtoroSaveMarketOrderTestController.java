package com.hkcapital.portoflio.broker.etoro.server;

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
public class EtoroSaveMarketOrderTestController
{
    @PostMapping("/saveOrder")
    public ResponseEntity<String> saveOrder(
            @RequestBody String orderJson,
            @RequestHeader Map<String, String> headers)
    {
        String order = "{\n" +
                "  \"orderForOpen\": {\n" +
                "    \"instrumentID\": 18,\n" +
                "    \"amount\": 50,\n" +
                "    \"isBuy\": true,\n" +
                "    \"leverage\": 20,\n" +
                "    \"stopLossRate\": 0,\n" +
                "    \"takeProfitRate\": 0,\n" +
                "    \"isTslEnabled\": false,\n" +
                "    \"mirrorID\": 0,\n" +
                "    \"totalExternalCosts\": 0,\n" +
                "    \"orderID\": 13902598,\n" +
                "    \"orderType\": 17,\n" +
                "    \"statusID\": 1,\n" +
                "    \"CID\": 7765437,\n" +
                "    \"openDateTime\": \"2025-04-02T15:47:15.9370502Z\",\n" +
                "    \"lastUpdate\": \"2025-04-02T15:47:15.9370502Z\"\n" +
                "  },\n" +
                "  \"token\": \"066faaee-e1e9-49d2-a568-c6e1cc336ad8\"\n" +
                "}";
        return ResponseEntity.ok(order);
    }


    @GetMapping("/portfolio")
    public ResponseEntity<String> portfolio() throws IOException
    {
        String data = Files.readString(Path.of("D:\\portfolio-pnl-simulator\\src\\test\\resources\\data\\portfolio\\portfolio_0_gold_open_orders.json"));
        System.out.println("Portfolio data : " + data);
        return ResponseEntity.ok(data);
    }


}
