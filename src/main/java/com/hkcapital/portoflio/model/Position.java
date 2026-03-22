package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "position")
@NoArgsConstructor
@AllArgsConstructor
@Data
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

    public Position(Integer recordIndex,
                    Strategy strategy,
                    Configuration configuraion, //
                    MarketConditions marketConditions,
                    Instrument instrument,
                    SRMatrix srMatrix,
                    TradingSessions tradingSessions,
                    Double percentCapitalDeployed,
                    Double currentPositionEquity, //
                    Double allowedFirePower, Double remainingFirepower, //
                    Double capitalRemainingFirePower, //
                    Double portfolioValue,
                    Integer leverage)
    {
        this.recordIndex = recordIndex;
        this.configuration = configuraion;
        this.marketConditions = marketConditions;
        this.instrument = instrument;
        this.srMatrix = srMatrix;
        this.tradingSessions = tradingSessions;
        this.percentCapitalDeployed = percentCapitalDeployed;
        this.currentPositionEquity = currentPositionEquity;
        this.allowedFirePower = allowedFirePower;
        this.remainingFirepower = remainingFirepower;
        this.capitalRemainingFirePower = capitalRemainingFirePower;
        this.portfolioValue = portfolioValue;
        this.strategy = strategy;
        this.leverage = leverage;
    }
}
