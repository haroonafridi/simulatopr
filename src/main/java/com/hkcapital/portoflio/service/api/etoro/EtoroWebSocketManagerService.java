package com.hkcapital.portoflio.service.api.etoro;

import com.hkcapital.portoflio.service.registry.Service;

public interface EtoroWebSocketManagerService extends Service
{
    void subscribeAndSchedule();
}
