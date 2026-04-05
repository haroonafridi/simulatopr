package com.hkcapital.portoflio.etoro;

import com.hkcapital.portoflio.repository.candle.CandleRepository;
import com.hkcapital.portoflio.repository.configuration.ConfigurationRepository;
import com.hkcapital.portoflio.repository.instrument.InstrumentRepository;
import com.hkcapital.portoflio.repository.marketconditions.MarketconditionsRepository;
import com.hkcapital.portoflio.repository.orders.etoro.EtoroOrderRepository;
import com.hkcapital.portoflio.repository.positions.PositionRepository;
import com.hkcapital.portoflio.repository.srmatrix.SRMatrixRepository;
import com.hkcapital.portoflio.repository.strategy.StrategyRepository;
import com.hkcapital.portoflio.repository.tradingsession.TradingSessionsRepository;
import com.hkcapital.portoflio.repository.tradingtimiframe.TradingTimeFramesRepository;
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
