package com.hkcapital.portflio.service.importexport.imprt.json.strategy;

import com.hkcapital.portflio.model.*;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.repository.srmatrix.SRMatrixFilter;
import com.hkcapital.portflio.service.configuration.dto.ConfigurationDTO;
import com.hkcapital.portflio.service.importexport.Importer;
import com.hkcapital.portflio.service.importexport.ImporterExporterAbstract;
import com.hkcapital.portflio.service.importexport.imprt.json.marketsructureconf.MarketStructureConfigurationImporter;
import com.hkcapital.portflio.service.importexport.imprt.json.srmatrix.SRMatrixImporter;
import com.hkcapital.portflio.service.importexport.imprt.json.srmatrixtolerance.SRMatrixToleranceImporter;
import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import com.hkcapital.portflio.service.marketconditions.dto.MarketConditionsDTO;
import com.hkcapital.portflio.service.positions.dto.PositionDTO;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixDTO;
import com.hkcapital.portflio.service.strategy.dto.StrategyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class StrategyImporter extends ImporterExporterAbstract implements Importer
{

    private final MarketStructureConfigurationImporter mrktStructureConfImporter;
    private final SRMatrixToleranceImporter srMatrixToleranceImporter;
    private final SRMatrixImporter srMatrixImporter;

    public StrategyImporter(final ServiceRegistery serviceRegistery,
                            final MarketStructureConfigurationImporter mrktStructureConfImporter,
                            final SRMatrixToleranceImporter srMatrixToleranceImporter,
                            final SRMatrixImporter srMatrixImporter)
    {
        super(serviceRegistery);
        this.mrktStructureConfImporter = mrktStructureConfImporter;
        this.srMatrixToleranceImporter = srMatrixToleranceImporter;
        this.srMatrixImporter = srMatrixImporter;
    }

    @Override
    public void importIn()
    {
        if (!envService.getActiveProfile().equals("simulation"))
        {
            log.info("Strategy cannot be imported in env = {}", envService.getActiveProfile());
            return;
        }

        log.info("Importing strategy in simulation env.");
        mrktStructureConfImporter.importIn();
        srMatrixToleranceImporter.importIn();
        srMatrixImporter.importIn();

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
                                .withFeed(instrumentDTO.getWithFeed())
                                .withBand(instrumentDTO.getWithBand())
                                .withCandle(instrumentDTO.getWithCandle())
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
                                    .instrument(instrument)
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
}
