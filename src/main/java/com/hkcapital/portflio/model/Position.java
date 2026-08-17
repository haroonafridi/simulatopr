package com.hkcapital.portflio.model;

import com.hkcapital.portflio.service.positions.PositionType;
import com.hkcapital.portflio.service.positions.dto.PositionDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "position")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Position implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "record_index")
    private Integer recordIndex;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "configuration_id", referencedColumnName = "id")
    private Configuration configuration;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "market_condition_id", referencedColumnName = "id")
    private MarketConditions marketConditions;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "instrument_id", referencedColumnName = "id")
    private Instrument instrument;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "sr_matrix_id", referencedColumnName = "id")
    private SRMatrix srMatrix;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "trading_session_id", referencedColumnName = "id")
    private TradingSessions tradingSessions;
    @Column(name = "percent_capital_deployed")
    private Double percentCapitalDeployed;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "strategy_id")
    private Strategy strategy;
    @Column(name = "current_position_equity")
    private Double currentPositionEquity;
    @Column(name = "allowed_fire_power")
    private Double allowedFirePower;
    @Column(name = "remaining_fire_power")
    private Double remainingFirepower;
    @Column(name = "capital_remaining_fire_power")
    private Double capitalRemainingFirePower;
    @Column(name = "portfolio_value")
    private Double portfolioValue;
    @Column(name = "leverage")
    private Integer leverage;
    @Column(name = "stop_loss")
    private Double stopLoss;
    @Column(name = "take_profit")
    private Double takeProfit;
    @Column(name = "active")
    private Boolean active = true;
    @Column(name = "position_type")
    private String positionType = PositionType.BUY.getValue();
    @Column(name = "execution_count")
    private Integer executionCount;

    public Integer getExecutionCount()
    {
        if (executionCount == null)
        {
            return 0;
        }
        return executionCount;
    }

    @Override
    public String toString()
    {
        return "/";
    }

    public PositionDTO buildPositionDTO()
    {
        return PositionDTO.builder()
                .configuration(configuration.buildDTO())
                .marketConditions(marketConditions.buildMarketConditionsDTO())
                .instrument(instrument.buildDto())
                .srMatrix(srMatrix.buildDTO())
                //.tradingSessions(tradingSessions.buildTradingSessionsDTO())
                .percentCapitalDeployed(percentCapitalDeployed)
                .currentPositionEquity(currentPositionEquity)
                .allowedFirePower(allowedFirePower)
                .remainingFirepower(remainingFirepower)
                .capitalRemainingFirePower(capitalRemainingFirePower)
                .portfolioValue(portfolioValue)
                .leverage(leverage)
                .stopLoss(stopLoss)
                .takeProfit(takeProfit)
                .active(active)
                .positionType(positionType)
                .executionCount(executionCount)
                .build();
    }
}
