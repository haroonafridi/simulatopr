package com.hkcapital.portflio.etoro.websocket.client;

import com.hkcapital.portflio.broker.etoro.config.EtoroApiConfiguration;
import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.market.indicators.Unit;
import com.hkcapital.portflio.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;


public class Gold1HourAutomaticOrdersStreategyE2ETest extends EtoroWebSocketClientAbstract_IT
{
    CountDownLatch done = new CountDownLatch(1);

    @Test
    void shouldCreateGoldBuyMarketOrder1HourTimeFrame() throws InterruptedException
    {
        strategyService.removeAll();
        marketConditionsService.removeAll();
        configurationService.removeAll();
        srMatrixService.removeAll();
        instrumentService.removeAll();
        etoroCandleService.removeAll();

        instrumentService.addInstrument(Instrument.builder().etoroInstrumentId(18)
                .instrumentDesc("Gold")
                .instrumentTicker("GOLD")
                .name("GOLD")
                .active(true)
                .maxSlippage(1.75d)
                .build());

        Instrument gold = instrumentService.findAll().stream().findFirst().get();

        configurationService.addConfiguration(Configuration.builder().lev(20)
                .noOfInsutrments(1)
                .noOfPositionsPerInstruments(3)
                .percentAllocationAllowed(15d)
                .maxPercentAllowedPerInstrument(7.5d)
                .build());

        Configuration goldConfig = configurationService.findAll().stream().findFirst().get();

        marketConditionsService.addMarketCondition(MarketConditions.builder()
                .instrument(gold)
                .percentMove(2d)
                .dayHigh(4500d)
                .dayLow(4400d)
                .build());

        MarketConditions goldMarketConditions = marketConditionsService.findAll().stream().findFirst().get();

        srMatrixService.addSRMatrix(SRMatrix.builder()
                .active(true)
                .resistance(4838d)
                .support(4673d)
                .timeFrameUnit(Unit.HOUR.getUnit())
                .timeFrame(1)
                .instrument(gold).build());

        SRMatrix srMatrix1Hour  = getSrMatrix(Unit.HOUR.getUnit(), 1);
        Strategy strategy = Strategy.builder()
                .description("Test Gold Strategy")
                .creationDate(LocalDateTime.now())
                .capitalAllocated(1500d)
                .active(true).
                build();

        Position pos1Hour = Position.builder()
                .instrument(gold)
                .leverage(20)
                .allowedFirePower(null)
                .capitalRemainingFirePower(null)
                .configuration(goldConfig)
                .currentPositionEquity(null)
                .marketConditions(goldMarketConditions)
                .currentPositionEquity(250d)
                .portfolioValue(null)
                .srMatrix(srMatrix1Hour)
                .strategy(strategy)
                .tradingSessions(null)
                .percentCapitalDeployed(null)
                .remainingFirepower(null)
                .stopLoss(10d)
                .takeProfit(30d)
                .build();
        strategy.setPositionPnLList(List.of(pos1Hour));
        strategyService.addStrategy(strategy);
        TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING = Boolean.TRUE;
        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);
        connect(gold);
        Thread.sleep(10000 * 16000);
    }

    private SRMatrix getSrMatrix(String unit, Integer interval)
    {
        return srMatrixService.findAll().stream().filter(matrix ->
                matrix.getTimeFrameUnit().equals(unit) &&
                        matrix.getTimeFrame().compareTo(interval) == 0).findFirst().get();
    }

    private String getAuthInfo(EtoroApiConfiguration apiInformation)
    {
        return """
                {
                  "id": "%s",
                  "operation": "Authenticate",
                  "data": {
                    "userKey": "%s",
                    "apiKey": "%s"
                  }
                }
                """.formatted(
                UUID.randomUUID(),
                apiInformation.getUserKey(),
                apiInformation.getApiKey()
        );
    }

}
