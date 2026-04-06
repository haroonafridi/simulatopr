package com.hkcapital.portoflio.service.candle.etoro;

import com.hkcapital.portoflio.broker.etoro.dto.candle.CandleResponseDto;
import com.hkcapital.portoflio.broker.etoro.master.TimeFrame;

public interface EtoroCandleResponseMapper
{
    CandleResponseDto mapResponse(Integer instrumentId, TimeFrame timeFrame, Integer interval );
}
