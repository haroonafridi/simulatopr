package com.hkcapital.portoflio.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "strategy")
public class Strategy
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    public Strategy(String description, LocalDateTime creationDate)
    {
        this.description = description;
        this.creationDate = creationDate;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LocalDateTime getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate)
    {
        this.creationDate = creationDate;
    }
}
