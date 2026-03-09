package com.hkcapital.portoflio.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "position")
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



    public Position()
    {
    }

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

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
    public Configuration getConfiguration()
    {
        return configuration;
    }

    public void setConfiguration(Configuration configuration)
    {
        this.configuration = configuration;
    }

    public MarketConditions getMarketConditions()
    {
        return marketConditions;
    }

    public void setMarketConditions(MarketConditions marketConditions)
    {
        this.marketConditions = marketConditions;
    }

    public Double getCurrentPositionEquity()
    {
        return currentPositionEquity;
    }

    public void setCurrentPositionEquity(Double currentPositionEquity)
    {
        this.currentPositionEquity = currentPositionEquity;
    }

    public Double getAllowedFirePower()
    {
        return allowedFirePower;
    }

    public void setAllowedFirePower(Double allowedFirePower)
    {
        this.allowedFirePower = allowedFirePower;
    }

    public Double getRemainingFirepower()
    {
        return remainingFirepower;
    }

    public void setRemainingFirepower(Double remainingFirepower)
    {
        this.remainingFirepower = remainingFirepower;
    }

    public Double getCapitalRemainingFirePower()
    {
        return capitalRemainingFirePower;
    }

    public void setCapitalRemainingFirePower(Double capitalRemainingFirePower)
    {
        this.capitalRemainingFirePower = capitalRemainingFirePower;
    }

    public Double getPortfolioValue()
    {
        return portfolioValue;
    }

    public void setPortfolioValue(Double portfolioValue)
    {
        this.portfolioValue = portfolioValue;
    }

    public Strategy getStrategy()
    {
        return strategy;
    }

    public void setStrategy(Strategy strategy)
    {
        this.strategy = strategy;
    }

    public Integer getRecordIndex()
    {
        return recordIndex;
    }

    public void setRecordIndex(Integer recordIndex)
    {
        this.recordIndex = recordIndex;
    }

    public Instrument getInstrument()
    {
        return instrument;
    }

    public Double getPercentCapitalDeployed()
    {
        return percentCapitalDeployed;
    }

    public void setPercentCapitalDeployed(Double percentCapitalDeployed)
    {
        this.percentCapitalDeployed = percentCapitalDeployed;
    }

    public void setInstrument(Instrument instrument)
    {
        this.instrument = instrument;
    }

    public SRMatrix getSrMatrix()
    {
        return srMatrix;
    }

    public void setSrMatrix(SRMatrix srMatrix)
    {
        this.srMatrix = srMatrix;
    }

    public TradingSessions getTradingSessions()
    {
        return tradingSessions;
    }

    public void setTradingSessions(TradingSessions tradingSessions)
    {
        this.tradingSessions = tradingSessions;
    }

    public Integer getLeverage()
    {
        return leverage;
    }

    public void setLeverage(Integer leverage)
    {
        this.leverage = leverage;
    }
}
