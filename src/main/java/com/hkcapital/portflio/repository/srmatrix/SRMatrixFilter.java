package com.hkcapital.portflio.repository.srmatrix;

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
public class SRMatrixFilter
{

    private LocalDateTime creationDate;

    private Integer timeFrame;

    private String timeFrameUnit;

    private Instrument instrument;

    private Double l_s_tolerance;

    private Double r_s_tolerance;

    private Double support;

    private Double resistance;

    private Double l_r_tolerance;

    private Double r_r_tolerance;

    private Double takeProfit;

    private Double stopLoss;
    private Boolean active;

}