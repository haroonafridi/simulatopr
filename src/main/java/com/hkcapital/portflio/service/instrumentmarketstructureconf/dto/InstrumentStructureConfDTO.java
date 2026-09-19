package com.hkcapital.portflio.service.instrumentmarketstructureconf.dto;

import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InstrumentStructureConfDTO
{
    private int marketOrder;
    private int module;
    private int sub;
    private int intrvl;
    private int timeFrame;
    private String timeFrameUnit;
    private boolean active;
    private LocalDateTime creationDate;
    private InstrumentDTO instrumentDto;
    private String structureName;
}
