package com.hkcapital.portflio.broker.etoro.simulation;

import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.model.Instrument;
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
import com.hkcapital.portflio.service.srmatrix.SRMatrixToleranceService;
import com.hkcapital.portflio.service.strategy.StrategyImportExportManager;
import com.hkcapital.portflio.service.strategy.StrategyImportExportManagerImpl;
import com.hkcapital.portflio.service.strategy.StrategyService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

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
        StrategyImportExportManager strategyImportExportManager = //
                new StrategyImportExportManagerImpl(serviceRegistery);
        strategyImportExportManager.importStrategy();
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
