package com.hkcapital.portflio.broker.etoro.simulation;

import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.market.indicators.TimeFramesUnit;
import com.hkcapital.portflio.model.*;
import com.hkcapital.portflio.repository.registry.ServiceRegistery;
import com.hkcapital.portflio.service.candle.etoro.EtoroCandleService;
import com.hkcapital.portflio.service.configuration.ConfigurationService;
import com.hkcapital.portflio.service.instrument.InstrumentService;
import com.hkcapital.portflio.service.marketconditions.MarketConditionsService;
import com.hkcapital.portflio.service.marketfeed.LiveInstrumentFeedService;
import com.hkcapital.portflio.service.orders.OrderManagerService;
import com.hkcapital.portflio.service.positions.PositionService;
import com.hkcapital.portflio.service.positions.PositionType;
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.service.srmatrix.SRMatrixToleranceService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

public class SimulationHelper
{
    private final ServiceRegistery serviceRegistery;
    private InstrumentService instrumentService;
    private LiveInstrumentFeedService liveInstrumentFeedService;
    private OrderManagerService orderManagerService;
    private StrategyService strategyService;
    private MarketConditionsService marketConditionsService;
    private ConfigurationService configurationService;
    private SRMatrixService sRMatrixService;
    private SRMatrixToleranceService sRMatrixToleranceService;
    private PositionService positionService;
    private EtoroCandleService etoroCandleService;
    private EtoroApiConfiguration etoroApiConfiguration;
    private RestClient restClient;


    public SimulationHelper(RestClient restClient,
                            ServiceRegistery serviceRegistery)
    {
        this.serviceRegistery = serviceRegistery;
        this.instrumentService = (InstrumentService) serviceRegistery.getService(Service.InstrumentService);
        this.liveInstrumentFeedService = (LiveInstrumentFeedService) serviceRegistery.getService(Service.LiveInstrumentFeedService);
        this.orderManagerService = (OrderManagerService) serviceRegistery.getService(Service.OrderManagerService);
        this.strategyService = (StrategyService) serviceRegistery.getService(Service.StrategyService);
        this.marketConditionsService = (MarketConditionsService) serviceRegistery.getService(Service.MarketConditionsService);
        this.configurationService = (ConfigurationService) serviceRegistery.getService(Service.ConfigurationService);
        this.sRMatrixService = (SRMatrixService) serviceRegistery.getService(Service.SRMatrixService);
        this.positionService = (PositionService) serviceRegistery.getService(Service.PositionService);
        this.etoroCandleService = (EtoroCandleService) serviceRegistery.getService(Service.EtoroCandleService);
        this.etoroApiConfiguration = (EtoroApiConfiguration) serviceRegistery.getService(Service.EtoroAPIConfiguration);
        this.sRMatrixToleranceService = (SRMatrixToleranceService) serviceRegistery.getService(Service.SRMatrixToleranceService);
        this.restClient = restClient;

    }

    @Transactional
    public void cleanAndInitPortfolio(double value)
    {

        positionService.removeAll();
        orderManagerService.removeAll();
        liveInstrumentFeedService.removeAll();
        configurationService.removeAll();
        marketConditionsService.removeAll();
        sRMatrixService.removeAll();
        sRMatrixToleranceService.removeAll();
        strategyService.removeAll();
        etoroCandleService.removeAll();
        orderManagerService.removeAll();
        instrumentService.removeAll();

        Instrument gold = instrumentService.addInstrument(Instrument.builder()
                .name("GOLD")
                .instrumentDesc("Gold Simulaution")
                .instrumentTicker("XAUUSD")
                .active(true)
                .maxSlippage(1.75)
                .etoroInstrumentId(18)
                .build());

        Strategy strategy = strategyService.addStrategy(Strategy.builder()
                .active(true)
                .description("Gold Test Strategy")
                .name("Gold Test Strategy")
                .capitalAllocated(10000d)
                .creationDate(LocalDateTime.now()).build());

        MarketConditions marketConditions = marketConditionsService.addMarketCondition(MarketConditions.builder()
                .instrument(gold)
                .dayLow(4300d)
                .dayHigh(4400d)
                .build());

        SRMatrix s15Min = sRMatrixService.addSRMatrix(SRMatrix.builder().active(true)
                .instrument(gold)
                .l_r_tolerance(4325d)
                .resistance(4330d)
                .l_r_tolerance(4335d)
                .support(4310d)
                .takeProfit(4325d)
                .stopLoss(4300d)
                .timeFrame(15)
                .creationDate(LocalDateTime.now())
                .timeFrameUnit(TimeFramesUnit.MINUTE.getUnit())
                .build());

        SRMatrix s4Hour = sRMatrixService.addSRMatrix(SRMatrix.builder().active(true)
                .instrument(gold)
                .l_r_tolerance(4350d)
                .resistance(4354d)
                .l_r_tolerance(4360d)
                .support(4310d)
                .takeProfit(4330d)
                .stopLoss(4300d)
                .timeFrame(4)
                .timeFrameUnit(TimeFramesUnit.HOUR.getUnit())
                .creationDate(LocalDateTime.now())
                .build());

        Configuration configuration = configurationService.addConfiguration(Configuration.builder()
                .lev(20)
                .maxPercentAllowedPerInstrument(2d)
                .percentAllocationAllowed(15d)
                .noOfInsutrments(1)
                .build());

        positionService.add(Position.builder()
                .currentPositionEquity(50d)
                .leverage(20)
                .instrument(gold)
                .srMatrix(s15Min)
                .strategy(strategy)
                .active(true)
                .executionCount(3)
                .positionType(PositionType.BUY.getValue())
                .marketConditions(marketConditions)
                .configuration(configuration).build());

        positionService.add(Position.builder()
                .currentPositionEquity(250d)
                .leverage(20)
                .instrument(gold)
                .srMatrix(s4Hour)
                .strategy(strategy)
                .executionCount(3)
                .positionType(PositionType.BUY.getValue())
                .active(true)
                .marketConditions(marketConditions)
                .configuration(configuration)
                .build());

        restClient.post().uri(etoroApiConfiguration.getSimulationPortfolioInit())
                .body(DepositDto.builder().initial(value)
                        .build()).retrieve().body(String.class);
    }

    public String getPortfolioValue()
    {
        return restClient.get()
                .uri(etoroApiConfiguration.getSimulationPortfolioValue())
                .retrieve().body(String.class);
    }

    public Instrument findInstrumentByEtoroId(int etoroId)
    {
        return instrumentService.findByEtoroInstrumentId(etoroId);
    }
}
