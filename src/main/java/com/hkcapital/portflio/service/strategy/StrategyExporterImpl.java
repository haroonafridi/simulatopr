package com.hkcapital.portflio.service.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hkcapital.portflio.model.Strategy;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.strategy.dto.StrategyDTO;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;

@Service
public class StrategyExporterImpl implements StrategyExporter
{
    private final ServiceRegistery serviceRegistery;
    private final InstrumentService instrumentService;
    private final StrategyService strategyService;

    public StrategyExporterImpl(ServiceRegistery serviceRegistery)
    {
        this.serviceRegistery = serviceRegistery;
        this.strategyService = (StrategyService) serviceRegistery.getService(StrategyService.StrategyService);
        this.instrumentService = (InstrumentService) serviceRegistery.getService(InstrumentService.InstrumentService);
    }

    @Override
    public void execute(int strategyId)
    {
        Strategy strategy = strategyService.findById(strategyId);
        ObjectWriter objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .writer()
                .withDefaultPrettyPrinter();
        try
        {
            StrategyDTO strategyDTO = strategy.buildStrategyDTO();
            final String json = objectMapper.writeValueAsString(strategyDTO);
            try
            {
                FileWriter fileWriter = new FileWriter(strategy.getName() + ".json");
                fileWriter.write(json);
                fileWriter.close();
                Strategy s2 = strategyDTO.buildStrategy();
                strategyService.addStrategy(s2);

            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }


    }
}
