package com.hkcapital.portflio.service.marketfeed.impl;

import com.hkcapital.portflio.model.LiveInstrumentFeed;
import com.hkcapital.portflio.repository.liveinstrumentfeed.LiveInstrumentFeedRepository;
import com.hkcapital.portflio.service.marketfeed.LiveInstrumentFeedService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
@Service
public class LiveInstrumentFeedServiceImpl implements LiveInstrumentFeedService
{
    private final LiveInstrumentFeedRepository liveInstrumentFeedRepository;

    public LiveInstrumentFeedServiceImpl(LiveInstrumentFeedRepository liveInstrumentFeedRepository)
    {
        this.liveInstrumentFeedRepository = liveInstrumentFeedRepository;
    }

    @Override
    public List<LiveInstrumentFeed> findByFeedDateBetween(Instant start, Instant end)
    {
        return liveInstrumentFeedRepository.findByFeedDateBetween(start, end);
    }
}
