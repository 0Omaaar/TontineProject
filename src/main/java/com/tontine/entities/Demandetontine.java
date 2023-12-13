package com.tontine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@MappedSuperclass
public class Demandetontine {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;

    private int montantPeriode;

    private int maxMembre;

    private Date dateDebut;

    private Date dateFin;

    private enum typeOrdre{
        MANUEL,
        AUTOMATIQUE
    }
    private enum statut{
        APPROUVE,
        REFUSE
    }

    @Column(nullable = false)
    private String frequence;

    @OneToOne
    private User user;
}
