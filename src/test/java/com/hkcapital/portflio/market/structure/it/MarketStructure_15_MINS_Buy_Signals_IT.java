package com.hkcapital.portflio.market.structure.it;

import com.hkcapital.portflio.broker.etoro.config.TradingConfiguration;
import com.hkcapital.portflio.etoro.websocket.client.EtoroWebSocketClientAbstract_IT;
import com.hkcapital.portflio.market.structure.MarketTypes;
import com.hkcapital.portflio.market.structure.Modus;
import com.hkcapital.portflio.market.structure.PreviousDayMarketRange;
import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.repository.liveinstrumentfeed.LiveInstrumentFeedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

public class MarketStructure_15_MINS_Buy_Signals_IT extends EtoroWebSocketClientAbstract_IT
{
    @Autowired
    private LiveInstrumentFeedRepository liveInstrumentFeedRepository;
    @Test
    public void shouldCreateBuySignal_gold_06_05() throws InterruptedException
    {
        liveInstrumentFeedRepository.deleteAll();
        getEtoroCandleService().removeAll();
        Instrument gold = Instrument.builder()
                .etoroInstrumentId(18)
                .build();
        final PreviousDayMarketRange
                priceRange = PreviousDayMarketRange.builder()
                .instrument(gold)
                .date(Instant.now())
                .low(4511.01)
                .high(4588.25)
                .build();

        final Modus modus = Modus.builder().mod(10).subtract(10).build();




        TradingConfiguration.ACTIVATE_AUTOMATIC_TRADING = Boolean.TRUE;

        marketFeedObserver.addMarketFeedSubscriber(marketFeedDbWriter);

        connect(gold);

        Thread.sleep(10000 * 16000);
    }
}
