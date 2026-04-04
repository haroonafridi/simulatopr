package com.hkcapital.portoflio.service.impl;

import com.hkcapital.portoflio.model.LiveInstrumentFeed;

public interface LiveInstrumentFeedDbService<LiveInstrumentRate>
{
   LiveInstrumentFeed save(LiveInstrumentRate liveInstrumentRate);
}
