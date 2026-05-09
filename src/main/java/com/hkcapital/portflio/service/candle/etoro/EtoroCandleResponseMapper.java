package com.hkcapital.portflio.service.candle.etoro;

import com.hkcapital.portflio.broker.etoro.dto.candle.CandleResponseDto;
import com.hkcapital.portflio.broker.etoro.master.TimeFrame;

public interface EtoroCandleResponseMapper
{
    CandleResponseDto mapResponse(Integer instrumentId, TimeFrame timeFrame, Integer interval );
}
