package com.tontine.modele;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
public class MembreTontine {
    @Id
    @GeneratedValue
    private int id;
    @NotBlank
    @Column(nullable = false)
    private Date dateadhesion;


}
