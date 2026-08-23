package com.hkcapital.portflio.service.strategy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hkcapital.portflio.model.*;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.repository.srmatrix.SRMatrixFilter;
import com.hkcapital.portflio.repository.srmatrix.SRMatrixToleranceFilter;
import com.hkcapital.portflio.service.configuration.ConfigurationService;
import com.hkcapital.portflio.service.configuration.dto.ConfigurationDTO;
import com.hkcapital.portflio.service.env.EnvService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portflio.service.marketconditions.dto.MarketConditionsDTO;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.positions.dto.PositionDTO;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.service.srmatrix.SRMatrixToleranceService;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixDTO;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixToleranceDTO;
import com.hkcapital.portflio.service.strategy.dto.StrategyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StrategyImportExportManagerImpl implements StrategyImportExportManager
{
    private static final Logger logger = LoggerFactory.getLogger(StrategyImportExportManager.class);
    private final ServiceRegistery serviceRegistery;
    private final InstrumentService instService;
    private final MarketConditionsService marketCondService;
    private final ConfigurationService configService;
    private final SRMatrixService sRMatrixService;
    private final SRMatrixToleranceService sRMatrixToleranceService;
    private final PositionService positionService;
    private final StrategyService strategyService;
    private final EnvService envService;

    ObjectReader objectReader = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .reader();

    ObjectWriter objectWriter = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .writer()
            .withDefaultPrettyPrinter();

    public StrategyImportExportManagerImpl(ServiceRegistery serviceRegistery)
    {
        this.serviceRegistery = serviceRegistery;
        this.strategyService = (StrategyService) serviceRegistery.getService(StrategyService.StrategyService);
        this.instService = (InstrumentService) serviceRegistery.getService(InstrumentService.InstrumentService);
        this.marketCondService = (MarketConditionsService) serviceRegistery.getService(MarketConditionsService.MarketConditionsService);
        this.configService = (ConfigurationService) serviceRegistery.getService(ConfigurationService.ConfigurationService);
        this.sRMatrixService = (SRMatrixService) serviceRegistery.getService(SRMatrixService.SRMatrixService);
        this.sRMatrixToleranceService = (SRMatrixToleranceService) serviceRegistery.getService(SRMatrixToleranceService.SRMatrixToleranceService);
        this.positionService = (PositionService) serviceRegistery.getService(PositionService.PositionService);
        this.envService = (EnvService) serviceRegistery.getService(EnvService.EnvService);
    }

    @Override
    public void exportStrategy(int strategyId)
    {
        exportSRMatrixTolerance();
        exportSRMatrix();
        Strategy strategy = strategyService.findById(strategyId);
        try
        {
            StrategyDTO strategyDTO = strategy.buildStrategyDTO();
            final String json = objectWriter.writeValueAsString(strategyDTO);
            try
            {
                FileWriter fileWriter = new FileWriter("D:/hk-simulation/strategies-export/" + strategyDTO.getName() + "-strategy.json");
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

        if (!envService.getActiveProfile().equals("simulation"))
        {
            logger.info("Strategy cannot be imported in env = {}", envService.getActiveProfile());
            return;
        }

        logger.info("Importing strategy in simulation env.");

        importSRMatrixTolerance();

        importSRMatrix();

        String dir = "D:/hk-simulation/strategies-imports/";
        try
        {
            Set<String> files = Stream.of(new File(dir).listFiles())
                    .filter(file -> !file.isDirectory() && file.getName().contains("strategy"))
                    .map(File::getName)
                    .collect(Collectors.toSet());

            for (String file : files)
            {

                StrategyDTO strategyDTO =
                        objectReader.readValue(new File(dir + file),
                                StrategyDTO.class);

                ArrayList<Position> positionList = new ArrayList<>();

                Strategy strategy = strategyService.addStrategy(Strategy.builder().active(strategyDTO.getActive())
                        .creationDate(strategyDTO.getCreationDate())
                        .capitalAllocated(strategyDTO.getCapitalAllocated())
                        .description(strategyDTO.getDescription())
                        .name(strategyDTO.getName())
                        .build());

                for (PositionDTO posDTO : strategyDTO.getPositionPnLList())
                {
                    InstrumentDTO instrumentDTO = posDTO.getInstrument();

                    Instrument instrument = instService.findByInstrumentTicker(instrumentDTO.getInstrumentTicker());

                    if (instrument == null)
                    {
                        instrument = instService.addInstrument(Instrument
                                .builder()
                                .name(instrumentDTO.getName())
                                .instrumentDesc(instrumentDTO.getInstrumentDesc())
                                .instrumentTicker(instrumentDTO.getInstrumentTicker())
                                .url(instrumentDTO.getUrl())
                                .maxSlippage(instrumentDTO.getMaxSlippage())
                                .etoroInstrumentId(instrumentDTO.getEtoroInstrumentId())
                                .active(instrumentDTO.getActive())
                                .build());
                    }

                    MarketConditionsDTO marketCondDTO = posDTO.getMarketConditions();

                    MarketConditions marketCond = marketCondService
                            .findByInstrumentAndDayLowAndDayHighAndPercentMove(instrument, marketCondDTO.getDayLow(),
                                    marketCondDTO.getDayHigh(), marketCondDTO.getPercentMove());

                    if (marketCond == null)
                    {
                        marketCond = marketCondService
                                .addMarketCondition(MarketConditions.builder().dayHigh(marketCondDTO.getDayHigh())
                                        .dayLow(marketCondDTO.getDayLow())
                                        .percentMove(marketCondDTO.getPercentMove())
                                        .instrument(instrument)
                                        .build());
                    }

                    ConfigurationDTO confDTO = posDTO.getConfiguration();

                    Configuration conf = configService.findByConfiguration(confDTO);

                    if (conf == null)
                    {
                        conf = configService.addConfiguration(Configuration.builder()
                                .noOfPositionsPerInstruments(confDTO.getNoOfPositionsPerInstruments())
                                .noOfInsutrments(confDTO.getNoOfInsutrments())
                                .lev(confDTO.getLev())
                                .maxPercentAllowedPerInstrument(confDTO.getMaxPercentAllowedPerInstrument())
                                .percentAllocationAllowed(confDTO.getPercentAllocationAllowed())
                                .build());

                    }

                    SRMatrixDTO srMatrixDTO = posDTO.getSrMatrix();

                    SRMatrixFilter srMatrixFilter =
                            SRMatrixFilter
                                    .builder()
                                    .l_s_tolerance(srMatrixDTO.getL_s_tolerance())
                                    .r_r_tolerance(srMatrixDTO.getR_r_tolerance())
                                    .r_s_tolerance(srMatrixDTO.getR_s_tolerance())
                                    .active(srMatrixDTO.getActive())
                                    .l_r_tolerance(srMatrixDTO.getL_r_tolerance())
                                    .resistance(srMatrixDTO.getResistance())
                                    .timeFrameUnit(srMatrixDTO.getTimeFrameUnit())
                                    .timeFrame(srMatrixDTO.getTimeFrame())
                                    .stopLoss(srMatrixDTO.getStopLoss())
                                    .support(srMatrixDTO.getSupport())
                                    .takeProfit(srMatrixDTO.getTakeProfit())
                                    .instrumentId(instrument.getId())
                                    .creationDate(srMatrixDTO.getCreationDate())
                                    .build();

                    List<SRMatrix> srMatrixList = sRMatrixService.findByFilter(srMatrixFilter);

                    SRMatrix srMatrix;

                    if (srMatrixList == null || srMatrixList.size() == 0)
                    {
                        srMatrix = sRMatrixService.addSRMatrix(SRMatrix.builder()
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
                    } else
                    {
                        srMatrix = srMatrixList.stream().findFirst().get();
                    }

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

    @Override
    public void exportSRMatrix()
    {
        List<SRMatrix> srMatrixList = sRMatrixService.findAll();

        List<SRMatrixDTO> sRMatrixDTO = new ArrayList<>();

        srMatrixList.stream().forEach(srMatrix ->
        {
            sRMatrixDTO.add(srMatrix.buildDTO());
        });
        try
        {
            final String json = objectWriter.writeValueAsString(sRMatrixDTO);
            FileWriter fileWriter = new FileWriter("D:/hk-simulation/strategies-export/sr-matrix/sr-matrix.json");
            fileWriter.write(json);
            fileWriter.close();

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void exportSRMatrixTolerance()
    {
        List<SRMatrixTolerance> sRMatrixToleranceList = sRMatrixToleranceService.findAll();

        List<SRMatrixToleranceDTO> SRMatrixToleranceDTOList = new ArrayList<>();

        sRMatrixToleranceList.stream().forEach(srMatrixTolerance ->
        {
            SRMatrixToleranceDTOList.add(srMatrixTolerance.buildSRMatrixToleranceDTO());
        });
        try
        {
            final String json = objectWriter.writeValueAsString(SRMatrixToleranceDTOList);
            FileWriter fileWriter = new FileWriter("D:/hk-simulation/strategies-export/sr-matrix-tolerance/sr-matrix-tolerance.json");
            fileWriter.write(json);
            fileWriter.close();

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void importSRMatrix()
    {
        String dir = "D:/hk-simulation/strategies-imports/sr-matrix/";
        Set<String> files = Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory() && file.getName().contains("sr-matrix"))
                .map(File::getName)
                .collect(Collectors.toSet());
        for (String file : files)
        {
            try
            {

                List<SRMatrixDTO> srMatrixDTOList =
                        objectReader.forType(new TypeReference<List<SRMatrixDTO>>()
                                {
                                })
                                .readValue(new File(dir + file));


                for (SRMatrixDTO sRMatrixDTO : srMatrixDTOList)
                {

                    Instrument instrument = instService.findByInstrumentTicker(sRMatrixDTO.getInstrumentDTO().getInstrumentTicker());

                    if (instrument == null)
                    {
                        InstrumentDTO instDTO = sRMatrixDTO.getInstrumentDTO();
                        instrument = instService
                                .addInstrument(Instrument.builder().instrumentTicker(instDTO.getInstrumentTicker())
                                        .etoroInstrumentId(instDTO.getEtoroInstrumentId())
                                        .name(instDTO.getName())
                                        .url(instDTO.getUrl())
                                        .maxSlippage(instDTO.getMaxSlippage())
                                        .active(instDTO.getActive())
                                        .instrumentDesc(instDTO.getInstrumentDesc())
                                        .build());
                    }
                    SRMatrixFilter srMatrixFilter =
                            SRMatrixFilter
                                    .builder()
                                    .l_s_tolerance(sRMatrixDTO.getL_s_tolerance())
                                    .r_r_tolerance(sRMatrixDTO.getR_r_tolerance())
                                    .r_s_tolerance(sRMatrixDTO.getR_s_tolerance())
                                    .active(sRMatrixDTO.getActive())
                                    .l_r_tolerance(sRMatrixDTO.getL_r_tolerance())
                                    .resistance(sRMatrixDTO.getResistance())
                                    .timeFrameUnit(sRMatrixDTO.getTimeFrameUnit())
                                    .timeFrame(sRMatrixDTO.getTimeFrame())
                                    .stopLoss(sRMatrixDTO.getStopLoss())
                                    .support(sRMatrixDTO.getSupport())
                                    .takeProfit(sRMatrixDTO.getTakeProfit())
                                    .instrumentId(instrument.getId())
                                    .creationDate(sRMatrixDTO.getCreationDate())
                                    .build();

                    List<SRMatrix> srMatrixList = sRMatrixService.findByFilter(srMatrixFilter);

                    if (srMatrixList == null || srMatrixList.size() == 0)
                    {
                        sRMatrixService
                                .addSRMatrix(SRMatrix.builder()
                                        .support(sRMatrixDTO.getSupport())
                                        .creationDate(sRMatrixDTO.getCreationDate())
                                        .active(sRMatrixDTO.getActive())
                                        .r_s_tolerance(sRMatrixDTO.getR_s_tolerance())
                                        .l_s_tolerance(sRMatrixDTO.getL_s_tolerance())
                                        .r_r_tolerance(sRMatrixDTO.getR_r_tolerance())
                                        .l_r_tolerance(sRMatrixDTO.getL_r_tolerance())
                                        .stopLoss(sRMatrixDTO.getStopLoss())
                                        .takeProfit(sRMatrixDTO.getTakeProfit())
                                        .timeFrameUnit(sRMatrixDTO.getTimeFrameUnit())
                                        .timeFrame(sRMatrixDTO.getTimeFrame())
                                        .resistance(sRMatrixDTO.getResistance())
                                        .instrument(instrument)
                                        .build());
                    }
                }


            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void importSRMatrixTolerance()
    {
        String dir = "D:/hk-simulation/strategies-imports/sr-matrix-tolerance/";
        Set<String> files = Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory() && file.getName().contains("sr-matrix-tolerance"))
                .map(File::getName)
                .collect(Collectors.toSet());
        for (String file : files)
        {
            try
            {

                List<SRMatrixToleranceDTO> sRMatrixToleranceDTOList =
                        objectReader.forType(new TypeReference<List<SRMatrixToleranceDTO>>()
                                {
                                })
                                .readValue(new File(dir + file));


                for (SRMatrixToleranceDTO sRMatrixToleranceDTO : sRMatrixToleranceDTOList)
                {

                    Instrument instrument = instService.findByInstrumentTicker(sRMatrixToleranceDTO.getInstrument().getInstrumentTicker());

                    if (instrument == null)
                    {
                        InstrumentDTO instDTO = sRMatrixToleranceDTO.getInstrument();
                        instrument = instService
                                .addInstrument(Instrument.builder().instrumentTicker(instDTO.getInstrumentTicker())
                                        .etoroInstrumentId(instDTO.getEtoroInstrumentId())
                                        .name(instDTO.getName())
                                        .url(instDTO.getUrl())
                                        .maxSlippage(instDTO.getMaxSlippage())
                                        .active(instDTO.getActive())
                                        .instrumentDesc(instDTO.getInstrumentDesc())
                                        .build());
                    }


                    SRMatrixToleranceFilter sRMatrixToleranceFilter =
                            SRMatrixToleranceFilter
                                    .builder()
                                    .r_s_tolerance_percent(sRMatrixToleranceDTO.getR_s_tolerance_percent())
                                    .l_r_tolerance_percent(sRMatrixToleranceDTO.getL_r_tolerance_percent())
                                    .l_s_tolerance_percent(sRMatrixToleranceDTO.getL_s_tolerance_percent())
                                    .r_r_tolerance_percent(sRMatrixToleranceDTO.getR_r_tolerance_percent())
                                    .timeFrameUnit(sRMatrixToleranceDTO.getTimeFrameUnit())
                                    .timeFrame(sRMatrixToleranceDTO.getTimeFrame())
                                    .instrumentId(instrument.getId())
                                    .stopLossPercent(sRMatrixToleranceDTO.getStopLossPercent())
                                    .takeProfitPercent(sRMatrixToleranceDTO.getTakeProfitPercent())
                                    .active(sRMatrixToleranceDTO.getActive())
                                    .creationDate(sRMatrixToleranceDTO.getCreationDate())
                                    .build();

                    List<SRMatrixTolerance> sRMatrixToleranceList = //
                            sRMatrixToleranceService.findByFilter(sRMatrixToleranceFilter);

                    if (sRMatrixToleranceList == null || sRMatrixToleranceList.size() == 0)
                    {
                        sRMatrixToleranceService
                                .addSRMatrixTolerance(SRMatrixTolerance.builder()
                                        .stopLossPercent(sRMatrixToleranceDTO.getStopLossPercent())
                                        .active(sRMatrixToleranceDTO.getActive())
                                        .l_r_tolerance_percent(sRMatrixToleranceDTO.getL_r_tolerance_percent())
                                        .l_s_tolerance_percent(sRMatrixToleranceDTO.getL_s_tolerance_percent())
                                        .r_r_tolerance_percent(sRMatrixToleranceDTO.getR_r_tolerance_percent())
                                        .r_r_tolerance_percent(sRMatrixToleranceDTO.getR_r_tolerance_percent())
                                        .takeProfitPercent(sRMatrixToleranceDTO.getTakeProfitPercent())
                                        .instrument(instrument)
                                        .timeFrame(sRMatrixToleranceDTO.getTimeFrame())
                                        .timeFrameUnit(sRMatrixToleranceDTO.getTimeFrameUnit())
                                        .creationDate(sRMatrixToleranceDTO.getCreationDate())
                                        .build());
                    }
                }


            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
