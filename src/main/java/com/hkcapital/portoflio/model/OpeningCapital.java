package com.hkcapital.portoflio.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


public class OpeningCapital
{
    int id; LocalDate date;  double capital;

    public OpeningCapital(int id, LocalDate date, double capital)
    {
        this.id = id;
        this.date = date;
        this.capital = capital;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public double getCapital()
    {
        return capital;
    }

    public void setCapital(double capital)
    {
        this.capital = capital;
    }
}
