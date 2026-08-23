package com.hkcapital.portflio.repository.srmatrix;

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
public class SRMatrixToleranceFilter
{

    private Integer instrumentId;

    private Integer timeFrame;

    private String timeFrameUnit;

    private Double l_s_tolerance_percent;

    private Double r_s_tolerance_percent;

    private Double l_r_tolerance_percent;

    private Double r_r_tolerance_percent;

    private Double takeProfitPercent;

    private Double stopLossPercent;
    private Boolean active;

    private LocalDateTime creationDate;

}