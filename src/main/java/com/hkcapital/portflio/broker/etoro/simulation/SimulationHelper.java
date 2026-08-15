package com.hkcapital.portflio.broker.etoro.simulation;

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
import com.hkcapital.portflio.service.registry.Service;
import com.hkcapital.portflio.service.srmatrix.SRMatrixService;
import com.hkcapital.portflio.service.strategy.StrategyService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

public class SimulationHelper
{

    private InstrumentService instrumentService;
    private LiveInstrumentFeedService liveInstrumentFeedService;
    private OrderManagerService orderManagerService;
    private StrategyService strategyService;
    private MarketConditionsService marketConditionsService;
    private ConfigurationService configurationService;
    private SRMatrixService sRMatrixService;
    private PositionService positionService;
    private EtoroCandleService etoroCandleService;
    private RestClient restClient;

    private final ServiceRegistery serviceRegistery;

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
        this.restClient = restClient;

    }

    @Transactional
    public void cleanAndInitPortfolio(double value)
    {

        orderManagerService.removeAll();
        liveInstrumentFeedService.removeAll();
        positionService.removeAll();
        configurationService.removeAll();
        marketConditionsService.removeAll();
        sRMatrixService.removeAll();
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
                .dayLow(4020d)
                .dayHigh(4111d)
                .build());

        SRMatrix s15Min = sRMatrixService.addSRMatrix(SRMatrix.builder().active(true)
                .instrument(gold)
                .resistance(4056d)
                .support(4028d)
                .timeFrame(15)
                .timeFrameUnit(TimeFramesUnit.MINUTE.getUnit())
                .build());

        SRMatrix s4Hour = sRMatrixService.addSRMatrix(SRMatrix.builder().active(true)
                .instrument(gold)
                .resistance(4105d)
                .support(4028d)
                .timeFrame(4)
                .timeFrameUnit(TimeFramesUnit.HOUR.getUnit())
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
                .marketConditions(marketConditions)
                .configuration(configuration).build());

        positionService.add(Position.builder()
                .currentPositionEquity(250d)
                .leverage(20)
                .instrument(gold)
                .srMatrix(s4Hour)
                .strategy(strategy)
                .active(true)
                .marketConditions(marketConditions)
                .configuration(configuration)
                .build());

        restClient.post().uri("http://localhost:8081/etoro/init")
                .body(DepositDto.builder().initial(value)
                        .build()).retrieve().body(String.class);
    }

    public String getPortfolioValue()
    {
        return restClient.get()
                .uri("http://localhost:8081/etoro/portfolio-value")
                .retrieve().body(String.class);
    }

    public Instrument findInstrumentByEtoroId(int etoroId)
    {
        return instrumentService.findByEtoroInstrumentId(etoroId);
    }
}
