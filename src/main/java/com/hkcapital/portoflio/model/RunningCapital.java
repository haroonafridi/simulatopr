package com.hkcapital.portoflio.model;

import jakarta.persistence.*;

@Entity
@Table(name ="running_capital")
public class RunningCapital
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "capital")
    private double capital;

    public RunningCapital(int id, double capital)
    {
        this.id = id;
        this.capital = capital;
    }

    public RunningCapital()
    {
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
