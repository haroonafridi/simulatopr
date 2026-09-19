package com.hkcapital.portflio.repository.instrumentmarketstructureconf;

import com.hkcapital.portflio.model.Instrument;
import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentMarketStructureConfFilter
{
    private String structureName;
}