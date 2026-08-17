package com.hkcapital.portflio.service.tradingsessions.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TradingSessionsDTO
{
    private String name;
    private String description;
    private String startTime;
    private String endTime;
}
