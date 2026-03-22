package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "brp_model")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BRPModel
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

    @Column(name = "support_hit_count")
    private Integer supportHitCount;

    @Column(name = "resistance_hit_count")
    private Integer resistanceHitCount;

    @Column(name = "rsi")
    private Double rsi;

    @Column(name = "bias")
    private Double bias;
}
