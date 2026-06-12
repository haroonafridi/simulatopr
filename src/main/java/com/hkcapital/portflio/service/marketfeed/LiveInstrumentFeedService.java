package com.hkcapital.portflio.service.marketfeed;

import com.hkcapital.portflio.model.LiveInstrumentFeed;
import com.hkcapital.portflio.service.registry.Service;

import java.time.Instant;
import java.util.List;

public interface LiveInstrumentFeedService extends Service
{
    List<LiveInstrumentFeed> findByFeedDateBetween(Instant start, Instant end);
}
