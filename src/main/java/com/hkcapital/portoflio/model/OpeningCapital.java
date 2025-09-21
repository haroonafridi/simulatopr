package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "opening_capital")
public class OpeningCapital
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "opening_date")
    LocalDate date;
    @Column(name = "opening_capital")
    double capital;

    public OpeningCapital()
    {
    }

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
