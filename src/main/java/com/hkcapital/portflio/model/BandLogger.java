package com.hkcapital.portflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "band_logger")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BandLogger
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String uuid;
    @Lob
    @Column(name = "band_desc", columnDefinition = "LONGTEXT")
    private String bandDesc;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

}
