package com.hkcapital.portoflio.model;

public class PositionPnL
{

    Integer index;
    Position position;
    Configuraion configuraion;
    MarketConditions marketConditions;
    Double percentPnL;
    Double pnl;
    Double currentPositionEquity;
    Double allowedFirePower;

    Double remainingFirepower;

    Double capitalRemainingFirePower;
    Double portfolioValue;

    public PositionPnL(Integer index, Position position, Configuraion configuraion, //
                       MarketConditions marketConditions, Double percentPnL, //
                       Double pnl, Double currentPositionEquity, //
                       Double allowedFirePower, Double remainingFirepower, //
                       Double capitalRemainingFirePower, Double portfolioValue)
    {
        this.index = index;
        this.position = position;
        this.configuraion = configuraion;
        this.marketConditions = marketConditions;
        this.percentPnL = percentPnL;
        this.pnl = pnl;
        this.currentPositionEquity = currentPositionEquity;
        this.allowedFirePower = allowedFirePower;
        this.remainingFirepower = remainingFirepower;
        this.capitalRemainingFirePower = capitalRemainingFirePower;
        this.portfolioValue = portfolioValue;
    }

    public Integer getIndex()
    {
        return index;
    }

    public void setIndex(Integer index)
    {
        this.index = index;
    }

    public Position getPosition()
    {
        return position;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    public Configuraion getConfiguraion()
    {
        return configuraion;
    }

    public void setConfiguraion(Configuraion configuraion)
    {
        this.configuraion = configuraion;
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
