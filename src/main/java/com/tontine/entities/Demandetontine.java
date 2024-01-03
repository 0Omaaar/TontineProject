package com.tontine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Date;


@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Demandetontine {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Veuillez Saisir Un Nom Valide !")
    private String nom;

    private int montantPeriode;

    private int maxMembre;

    private LocalDate dateDebut;

    private LocalDate dateFin;


    private enum TypeOrdre{
        ALEATOIRE,
        ORDER
    }
    @Enumerated(EnumType.STRING)
    private TypeOrdre typeOrdre;
    public enum StatutDemande{
        EN_ATTENTE,
        APPROUVE,
        REFUSE
    }
    @Enumerated(EnumType.STRING)
    private StatutDemande statutDemande;

    public enum Frequence {
        HEBDOMADAIRE,
        MENTUEL,
        TRIMENTUEL
    }
    @Enumerated(EnumType.STRING)
    private Frequence frequence;


//    @Column(nullable = false)
//    private String frequence;
//
//    @ManyToOne
//    private Utilisateur utilisateur;
//
//    @ManyToOne
//    private Tontine tontine;

}
