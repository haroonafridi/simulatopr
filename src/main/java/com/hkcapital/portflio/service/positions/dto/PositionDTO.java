package com.hkcapital.portflio.service.positions.dto;


import com.hkcapital.portflio.service.configuration.dto.ConfigurationDTO;
import com.hkcapital.portflio.service.instrument.dto.InstrumentDTO;
import com.hkcapital.portflio.service.marketconditions.dto.MarketConditionsDTO;
import com.hkcapital.portflio.service.srmatrix.dto.SRMatrixDTO;
import com.hkcapital.portflio.service.strategy.dto.StrategyDTO;
import com.hkcapital.portflio.service.tradingsessions.dto.TradingSessionsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PositionDTO implements Serializable
{

    private ConfigurationDTO configuration;
    private MarketConditionsDTO marketConditions;
    private InstrumentDTO instrument;
    private SRMatrixDTO srMatrix;
    private TradingSessionsDTO tradingSessions;
    private Double percentCapitalDeployed;
    private StrategyDTO strategy;
    private Double currentPositionEquity;
    private Double allowedFirePower;
    private Double remainingFirepower;
    private Double capitalRemainingFirePower;
    private Double portfolioValue;
    private Integer leverage;
    private Double stopLoss;
    private Double takeProfit;
    private Boolean active = true;
    private String positionType;
    private Integer executionCount;

}
