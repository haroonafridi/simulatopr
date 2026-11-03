package com.hkcapital.portflio.repository.tradingindicators;

import com.hkcapital.portflio.model.Instrument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingIndicatorsFilter
{
    private String name;

    private Integer period;
    private Integer timeFrame;
    private String timeFrameUnit;
    private Instrument instrument;

}