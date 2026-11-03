package com.hkcapital.portflio.service.tradingindicators.dto;

import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TradingIndicatorDTO
{
    private InstrumentDTO instrument;
    private String name;
    private int timeFrame;
    private String timeFrameUnit;
    private int period;
}
