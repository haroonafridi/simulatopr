package com.hkcapital.portflio.service.instrument;

import com.hkcapital.portflio.model.LiveInstrumentFeed;

public interface LiveInstrumentFeedDbService<LiveInstrumentRate>
{
   LiveInstrumentFeed save(LiveInstrumentRate liveInstrumentRate);
}
