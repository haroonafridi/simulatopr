package com.hkcapital.portflio.service.marketconditions.dto;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MarketConditionsDTO
{
    private InstrumentDTO instrumentDTO;
    private Double dayLow;
    private Double dayHigh;
    private Double percentMove;
}
