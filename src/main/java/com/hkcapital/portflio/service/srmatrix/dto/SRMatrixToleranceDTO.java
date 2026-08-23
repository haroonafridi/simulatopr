package com.hkcapital.portflio.service.srmatrix.dto;

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
public class SRMatrixToleranceDTO
{
    private InstrumentDTO instrument;

    private Integer timeFrame;

    private String timeFrameUnit;

    private Double l_s_tolerance_percent = 0.0;

    private Double r_s_tolerance_percent = 0.0;

    private Double l_r_tolerance_percent = 0.0;

    private Double r_r_tolerance_percent = 0.0;

    private Double takeProfitPercent = 0.0;

    private Double stopLossPercent = 0.0;
    private Boolean active = true;

    private LocalDateTime creationDate;

}