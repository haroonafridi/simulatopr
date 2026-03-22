package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "opening_capital")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OpeningCapital
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "opening_date")
    LocalDate date;
    @Column(name = "opening_capital")
    double capital;
}
