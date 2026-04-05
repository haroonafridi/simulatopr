package com.hkcapital.portoflio.repository.liveinstrumentfeed;

import com.hkcapital.portoflio.model.Candle;
import com.hkcapital.portoflio.model.LiveInstrumentFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveInstrumentFeedRepository extends JpaRepository<LiveInstrumentFeed, Integer>
{

}
