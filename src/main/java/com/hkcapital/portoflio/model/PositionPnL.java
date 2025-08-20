package com.hkcapital.portoflio.model;

public record PositionPnL(int index, Position position, Configuraion configuraion, //
                          MarketConditions marketConditions, double percentPnL, double pnl, double currentPositionEquity,
                          double allowedFirePower,

                          double remainingFirepower,

                          double capitalRemainingFirePower,
                          double portfolioValue)
{


}
