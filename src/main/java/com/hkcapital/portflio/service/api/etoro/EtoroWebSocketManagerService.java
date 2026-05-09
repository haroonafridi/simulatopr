package com.hkcapital.portflio.service.api.etoro;

import com.hkcapital.portflio.service.registry.Service;

public interface EtoroWebSocketManagerService extends Service
{
    void subscribeAndSchedule();
}
