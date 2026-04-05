package com.hkcapital.portoflio.service.instrument;

import com.hkcapital.portoflio.model.LiveInstrumentFeed;

public interface LiveInstrumentFeedDbService<LiveInstrumentRate>
{
   LiveInstrumentFeed save(LiveInstrumentRate liveInstrumentRate);
}
