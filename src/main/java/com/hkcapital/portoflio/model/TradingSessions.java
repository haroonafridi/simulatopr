package com.hkcapital.portoflio.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trading_sessions")
public class TradingSessions
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
    @Column(name = "start_time")

    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    public TradingSessions()
    {
    }

    public TradingSessions(String name, String description, String startTime, String endTime)
    {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }
}
