package com.hkcapital.portoflio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "strategy")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Strategy
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 200)
    private String name;
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "capital_allocated")
    private Double capitalAllocated;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "active")
    private Boolean active;
    @OneToMany(mappedBy = "strategy", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Position> positionPnLList;

    public Strategy(final String name,
                    final String description,  //
                    final LocalDateTime creationDate
                    )
    {
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
    }
}
