package com.hkcapital.portoflio.repository.liveinstrumentfeed;

import com.hkcapital.portoflio.model.LiveInstrumentFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface LiveInstrumentFeedRepository extends JpaRepository<LiveInstrumentFeed, Integer>
{
    List<LiveInstrumentFeed> findByFeedDateBetween(Instant start, Instant end);
}
