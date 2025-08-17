package com.hkcapital.portoflio;

public record PositionPnL(int index, Position position, Configuraion configuraion, //
                          MarketConditions marketConditions, double percentPnL, double pnl, double currentPositionEquity,
                          double portfolioValue)
{


}
