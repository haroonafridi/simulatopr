package com.hkcapital.portflio.model;

import com.hkcapital.portflio.model.Instrument;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "market_structure_conf")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstrumentMarketStructureConf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "market_order")
    private int marketOrder;
    @Column(name = "module")
    private int module;
    @Column(name = "sub")
    private int sub;

    @Column(name = "intrvl")
    private int intrvl;

    @Column(name = "time_frame")
    private int timeFrame;

    @Column(name = "time_frame_unit")
    private String timeFrameUnit;

    @Column(name = "active")
    private boolean active;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "instrument_id")
    private Instrument instrument;
    @Column(name = "structure_name")
    private String structureName;
}

