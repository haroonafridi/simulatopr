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
    private Configuration configurtaion;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "market_condition_id", referencedColumnName = "id")
    private MarketConditions marketConditions;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "instrument_id", referencedColumnName = "id")
    private Instrument instrument;
    @Column(name = "percent_capital_deployed")
    private Double percentCapitalDeployed;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "strategy_id")
    private Strategy strategy;
    @Column(name = "percent_pnl")
    private Double percentPnL;
    @Column(name = "pnl")
    private Double pnl;
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

    public Position()
    {
    }

    public Position(Integer recordIndex,
                    Strategy strategy,
                    Configuration configuraion, //
                    MarketConditions marketConditions,
                    Instrument instrument,
                    Double percentPnL, //
                    Double percentCapitalDeployed,
                    Double pnl, Double currentPositionEquity, //
                    Double allowedFirePower, Double remainingFirepower, //
                    Double capitalRemainingFirePower, Double portfolioValue)
    {
        this.recordIndex = recordIndex;
        this.configurtaion = configuraion;
        this.marketConditions = marketConditions;
        this.instrument = instrument;
        this.percentCapitalDeployed = percentCapitalDeployed;
        this.percentPnL = percentPnL;
        this.pnl = pnl;
        this.currentPositionEquity = currentPositionEquity;
        this.allowedFirePower = allowedFirePower;
        this.remainingFirepower = remainingFirepower;
        this.capitalRemainingFirePower = capitalRemainingFirePower;
        this.portfolioValue = portfolioValue;
        this.strategy = strategy;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
    public Configuration getConfigurtaion()
    {
        return configurtaion;
    }

    public void setConfigurtaion(Configuration configurtaion)
    {
        this.configurtaion = configurtaion;
    }

    public MarketConditions getMarketConditions()
    {
        return marketConditions;
    }

    public void setMarketConditions(MarketConditions marketConditions)
    {
        this.marketConditions = marketConditions;
    }

    public Double getPercentPnL()
    {
        return percentPnL;
    }

    public void setPercentPnL(Double percentPnL)
    {
        this.percentPnL = percentPnL;
    }

    public Double getPnl()
    {
        return pnl;
    }

    public void setPnl(Double pnl)
    {
        this.pnl = pnl;
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
}
