package com.hkcapital.portflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sr_matrix_tolerance")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SRMatrixTolerance
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "instrument_id", referencedColumnName = "id")
    private Instrument instrument;
    @Column(name = "time_frame")
    private Integer timeFrame;
    @Column(name = "time_frame_unit")
    private String timeFrameUnit;
    @Column(name = "l_s_tolerance_percent")
    private Double l_s_tolerance_percent = 0.0;
    @Column(name = "r_s_tolerance_percent")
    private Double r_s_tolerance_percent = 0.0;
    @Column(name = "l_r_tolerance_percent")
    private Double l_r_tolerance_percent = 0.0;
    @Column(name = "r_r_tolerance_percent")
    private Double r_r_tolerance_percent = 0.0;
    @Column(name = "take_profit_percent")
    private Double takeProfitPercent = 0.0;
    @Column(name = "stop_loss_percent")
    private Double stopLossPercent = 0.0;
    private Boolean active = true;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

}