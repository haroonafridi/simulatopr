package com.hkcapital.portflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sr_matrix")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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
    @Column(name = "l_s_tolerance")
    private Double l_s_tolerance;
    @Column(name = "r_s_tolerance")
    private Double r_s_tolerance;
    @Column(name = "support")
    private Double support;
    @Column(name = "resistance")
    private Double resistance;
    @Column(name = "l_r_tolerance")
    private Double l_r_tolerance;
    @Column(name = "r_r_tolerance")
    private Double r_r_tolerance;

    @Column(name = "take_profit")
    private Double takeProfit;

    @Column(name = "stop_loss")
    private Double stopLoss;
    private Boolean active;

    public Double getLeftResistanceTolerance()
    {
        if (l_r_tolerance == null)
        {
            return 0d;
        }
        return l_r_tolerance;
    }

    public Double getRightResistanceTolerance()
    {
        if (r_r_tolerance == null)
        {
            return 0d;
        }
        return r_r_tolerance;
    }

    public Double getLeftSupportTolerance()
    {
        if (l_s_tolerance == null)
        {
            return 0d;
        }
        return l_s_tolerance;
    }

    public Double getRightSupportTolerance()
    {
        if (r_s_tolerance == null)
        {
            return 0d;
        }
        return r_s_tolerance;
    }
}