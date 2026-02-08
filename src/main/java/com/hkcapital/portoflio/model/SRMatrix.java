package com.hkcapital.portoflio.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "sr_matrix")
public class SRMatrix
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "time_frame")
    private Integer timeFrame;
    @Column(name = "time_frame_unit")
    private String timeFrameUnit;
    @Column(name = "instrument")
    private String instrument;

    @Column(name = "support")
    private Double support;

    @Column(name = "resistance")
    private Double resistance;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public LocalDateTime getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate)
    {
        this.creationDate = creationDate;
    }

    public Integer getTimeFrame()
    {
        return timeFrame;
    }

    public void setTimeFrame(Integer timeFrame)
    {
        this.timeFrame = timeFrame;
    }

    public String getTimeFrameUnit()
    {
        return timeFrameUnit;
    }

    public void setTimeFrameUnit(String timeFrameUnit)
    {
        this.timeFrameUnit = timeFrameUnit;
    }

    public String getInstrument()
    {
        return instrument;
    }

    public void setInstrument(String instrument)
    {
        this.instrument = instrument;
    }

    public Double getSupport()
    {
        return support;
    }

    public void setSupport(Double support)
    {
        this.support = support;
    }

    public Double getResistance()
    {
        return resistance;
    }

    public void setResistance(Double resistance)
    {
        this.resistance = resistance;
    }
}
