package com.hkcapital.portoflio.etoro;

import com.hkcapital.portoflio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbCleaner
{
    @Autowired
    private InstrumentRepository instrumentRepository;
    @Autowired
    private CandleRepository candleRepository;
    @Autowired
    private ConfigurationRepository configurationRepository;
    @Autowired
    private MarketconditionsRepository marketconditionsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired

    private PositionRepository positionRepository;
    @Autowired

    private SRMatrixRepository srMatrixRepository;
    @Autowired

    private StrategyRepository strategyRepository;
    @Autowired

    private TradingSessionsRepository tradingSessionsRepository;
    @Autowired

    private TradingTimeFramesRepository tradingTimeFramesRepository;

    public void clean()
    {
        positionRepository.deleteAll();
        strategyRepository.deleteAll();
        srMatrixRepository.deleteAll();
        tradingSessionsRepository.deleteAll();
        tradingTimeFramesRepository.deleteAll();
        marketconditionsRepository.deleteAll();
        orderRepository.deleteAll();
        candleRepository.deleteAll();
        instrumentRepository.deleteAll();
    }
}
