package com.hkcapital.portoflio.model;

public class RunningCapital
{
    int id;
    double capital;

    public RunningCapital(int id, double capital)
    {
        this.id = id;
        this.capital = capital;
    }

    public int getId()
    {
        return id;
    }

    public double getCapital()
    {
        return capital;
    }
}
