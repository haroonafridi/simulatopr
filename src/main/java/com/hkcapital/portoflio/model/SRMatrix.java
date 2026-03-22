package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sr_matrix")
@NoArgsConstructor
@AllArgsConstructor
@Data
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


    public SRMatrix(LocalDateTime creationDate, //
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
}