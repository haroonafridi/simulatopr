package com.hkcapital.portflio.service.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hkcapital.portflio.model.*;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.configuration.ConfigurationService;
import com.hkcapital.portflio.service.configuration.dto.ConfigurationDTO;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portflio.service.marketconditions.dto.MarketConditionsDTO;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.positions.dto.PositionDTO;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixDTO;
import com.hkcapital.portflio.service.strategy.dto.StrategyDTO;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StrategyImportExportManagerImpl implements StrategyImportExportManager
{
    private final ServiceRegistery serviceRegistery;
    private final InstrumentService instService;
    private final MarketConditionsService marketCondService;
    private final ConfigurationService configService;
    private final SRMatrixService sRMatrixService;
    private final PositionService positionService;
    private final StrategyService strategyService;

    public StrategyImportExportManagerImpl(ServiceRegistery serviceRegistery)
    {
        this.serviceRegistery = serviceRegistery;
        this.strategyService = (StrategyService) serviceRegistery.getService(StrategyService.StrategyService);
        this.instService = (InstrumentService) serviceRegistery.getService(InstrumentService.InstrumentService);
        this.marketCondService = (MarketConditionsService) serviceRegistery.getService(MarketConditionsService.MarketConditionsService);
        this.configService = (ConfigurationService) serviceRegistery.getService(ConfigurationService.ConfigurationService);
        this.sRMatrixService = (SRMatrixService) serviceRegistery.getService(SRMatrixService.SRMatrixService);
        this.positionService = (PositionService) serviceRegistery.getService(PositionService.PositionService);
    }

    @Override
    public void exportStrategy(int strategyId)
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
                FileWriter fileWriter = new FileWriter("D:/hk-simulation/strategies-export/" + strategyDTO.getName() + ".json");
                fileWriter.write(json);
                fileWriter.close();

            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        } catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void importStrategy()
    {

        ObjectReader objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .reader();
        String dir = "D:/hk-prod/strategies-imports/";
        try
        {
            Set<String> files = Stream.of(new File(dir).listFiles())
                    .filter(file -> !file.isDirectory() && file.getName().endsWith(".json"))
                    .map(File::getName)
                    .collect(Collectors.toSet());

            for (String file : files)
            {

                StrategyDTO strategyDTO = objectMapper.readValue(new File(dir+file), StrategyDTO.class);
                ArrayList<Position> positionList = new ArrayList<>();

                Strategy strategy = strategyService.addStrategy(Strategy.builder().active(strategyDTO.getActive())
                        .creationDate(strategyDTO.getCreationDate())
                        .capitalAllocated(strategyDTO.getCapitalAllocated())
                        .description(strategyDTO.getDescription())
                        .name(strategyDTO.getName())
                        .build());

                InstrumentDTO instrumentDTO = strategyDTO.getPositionPnLList().stream().findAny().get().getInstrument();

                Instrument instrument = instService.addInstrument(Instrument
                        .builder()
                        .name(instrumentDTO.getName())
                        .instrumentDesc(instrumentDTO.getInstrumentDesc())
                        .instrumentTicker(instrumentDTO.getInstrumentTicker())
                        .url(instrumentDTO.getUrl())
                        .maxSlippage(instrumentDTO.getMaxSlippage())
                        .etoroInstrumentId(instrumentDTO.getEtoroInstrumentId())
                        .active(instrumentDTO.getActive())
                        .build());


                for (PositionDTO posDTO : strategyDTO.getPositionPnLList())
                {
                    MarketConditionsDTO marketCondDTO = posDTO.getMarketConditions();

                    MarketConditions marketCond = marketCondService
                            .addMarketCondition(MarketConditions.builder().dayHigh(marketCondDTO.getDayHigh())
                                    .dayLow(marketCondDTO.getDayLow())
                                    .percentMove(marketCondDTO.getPercentMove())
                                    .instrument(instrument)
                                    .build());

                    ConfigurationDTO confDTO = posDTO.getConfiguration();

                    Configuration conf = configService.addConfiguration(Configuration.builder()
                            .noOfPositionsPerInstruments(confDTO.getNoOfPositionsPerInstruments())
                            .noOfInsutrments(confDTO.getNoOfInsutrments())
                            .lev(confDTO.getLev())
                            .maxPercentAllowedPerInstrument(confDTO.getMaxPercentAllowedPerInstrument())
                            .percentAllocationAllowed(confDTO.getPercentAllocationAllowed())
                            .build());

                    SRMatrixDTO srMatrixDTO = posDTO.getSrMatrix();
                    SRMatrix srMatrix = sRMatrixService.addSRMatrix(SRMatrix.builder()
                            .creationDate(srMatrixDTO.getCreationDate())
                            .timeFrame(srMatrixDTO.getTimeFrame())
                            .timeFrameUnit(srMatrixDTO.getTimeFrameUnit())
                            .takeProfit(srMatrixDTO.getTakeProfit())
                            .stopLoss(srMatrixDTO.getStopLoss())
                            .l_r_tolerance(srMatrixDTO.getL_r_tolerance())
                            .l_s_tolerance(srMatrixDTO.getL_s_tolerance())
                            .resistance(srMatrixDTO.getResistance())
                            .support(srMatrixDTO.getSupport())
                            .r_r_tolerance(srMatrixDTO.getR_r_tolerance())
                            .r_s_tolerance(srMatrixDTO.getR_s_tolerance())
                            .instrument(instrument)
                            .active(srMatrixDTO.getActive())
                            .build());

                    Position pos = positionService.add(Position.builder()
                            .executionCount(posDTO.getExecutionCount())
                            .positionType(posDTO.getPositionType())
                            .leverage(posDTO.getLeverage())
                            .portfolioValue(posDTO.getPortfolioValue())
                            .currentPositionEquity(posDTO.getCurrentPositionEquity())
                            .remainingFirepower(posDTO.getRemainingFirepower())
                            .strategy(strategy)
                            .marketConditions(marketCond)
                            .allowedFirePower(posDTO.getAllowedFirePower())
                            .percentCapitalDeployed(posDTO.getPercentCapitalDeployed())
                            .srMatrix(srMatrix)
                            .active(posDTO.getActive())
                            .tradingSessions(null)
                            .takeProfit(posDTO.getTakeProfit())
                            .stopLoss(posDTO.getStopLoss())
                            .instrument(instrument)
                            .configuration(conf)
                            .capitalRemainingFirePower(posDTO.getCapitalRemainingFirePower())
                            .build());
                    positionList.add(pos);
                }
                strategy.setPositionPnLList(positionList);
                strategyService.addStrategy(strategy);
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}
