package com.hkcapital.portoflio.model;

import jakarta.persistence.*;

@Entity
@Table(name ="position_pnl")
public class PositionPnL
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "position_id", referencedColumnName = "id")
    Position position;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "configuration_id", referencedColumnName = "id")
    Configuration configurtaion;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "market_condition_id", referencedColumnName = "id")
    MarketConditions marketConditions;
    @Column(name = "percent_pnl")
    Double percentPnL;
    @Column(name = "pnl")
    Double pnl;
    @Column(name = "current_position_equity")
    Double currentPositionEquity;
    @Column(name = "allowed_fire_power")
    Double allowedFirePower;

    @Column(name = "remaining_fire_power")
    Double remainingFirepower;
    @Column(name = "capital_remaining_fire_power")
    Double capitalRemainingFirePower;
    @Column(name = "portfolio_value")
    Double portfolioValue;

    public PositionPnL(Integer index, Position position, Configuration configuraion, //
                       MarketConditions marketConditions, Double percentPnL, //
                       Double pnl, Double currentPositionEquity, //
                       Double allowedFirePower, Double remainingFirepower, //
                       Double capitalRemainingFirePower, Double portfolioValue)
    {
        this.id = index;
        this.position = position;
        this.configurtaion = configuraion;
        this.marketConditions = marketConditions;
        this.percentPnL = percentPnL;
        this.pnl = pnl;
        this.currentPositionEquity = currentPositionEquity;
        this.allowedFirePower = allowedFirePower;
        this.remainingFirepower = remainingFirepower;
        this.capitalRemainingFirePower = capitalRemainingFirePower;
        this.portfolioValue = portfolioValue;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
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
}
