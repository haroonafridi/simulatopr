package com.hkcapital.portoflio.broker.etoro;

public class CalcUtils
{
    public static double calculateTargetPrice(double openPrice, int leverage, double investment, double tpAmount) {
        // The amount of price movement needed to reach the profit goal
        double priceMove = (tpAmount / (investment * leverage)) * openPrice;

        System.out.println("openPrice =["+openPrice+"] tpAmount =["+tpAmount+"] leverage = ["+leverage+"]  investment= ["+investment+"] price move = ["+priceMove+"]");
        return openPrice + priceMove;
    }

    public static void main(String[] args)
    {
        double open = 65581.28;
        int lev = 1;
        double inv = 50.0;
        double tp = 4;
        double result = Math.round(calculateTargetPrice(open, lev, inv, tp));
        // Asserting with a small delta for rounding
        System.out.println(result);
    }
}
