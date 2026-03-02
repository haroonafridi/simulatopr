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
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "instrument_id", referencedColumnName = "id")
    private Instrument instrument;

    @Column(name = "support")
    private Double support;

    @Column(name = "resistance")
    private Double resistance;

    private Boolean active;

    public SRMatrix() {

    }
    public SRMatrix( LocalDateTime creationDate, //
                     Integer timeFrame, //
                     String timeFrameUnit, //
                     Instrument instrument, //
                     Double support, //
                     Double resistance,
                     Boolean active)
    {
        this.creationDate = creationDate;
        this.timeFrame = timeFrame;
        this.timeFrameUnit = timeFrameUnit;
        this.instrument = instrument;
        this.support = support;
        this.resistance = resistance;
        this.active = active;
    }

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

    public Instrument getInstrument()
    {
        return instrument;
    }

    public void setInstrument(Instrument instrument)
    {
        this.instrument = instrument;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }
}
