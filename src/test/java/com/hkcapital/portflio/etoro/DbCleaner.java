package com.hkcapital.portflio.etoro;

import com.hkcapital.portflio.repository.candle.CandleRepository;
import com.hkcapital.portflio.repository.configuration.ConfigurationRepository;
import com.hkcapital.portflio.repository.instrument.InstrumentRepository;
import com.hkcapital.portflio.repository.marketconditions.MarketConditionsRepository;
import com.hkcapital.portflio.repository.orders.etoro.EtoroOrderRepository;
import com.hkcapital.portflio.repository.positions.PositionRepository;
import com.hkcapital.portflio.repository.srmatrix.SRMatrixRepository;
import com.hkcapital.portflio.repository.strategy.StrategyRepository;
import com.hkcapital.portflio.repository.tradingsession.TradingSessionsRepository;
import com.hkcapital.portflio.repository.tradingtimiframe.TradingTimeFramesRepository;
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
    private MarketConditionsRepository marketconditionsRepository;
    @Autowired
    private EtoroOrderRepository orderRepository;
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
