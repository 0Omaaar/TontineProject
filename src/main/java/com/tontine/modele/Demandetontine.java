package com.tontine.modele;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class Demandetontine {
    @Id
    @GeneratedValue
    private int id;
    private String nom;
    private int montantPeriode;
    private int maxMembre;
    private Date dateDebut;
    private Date dateFin;
    private enum typeOrdre{
        manuel,
        automatique
    }
    private enum statut{
        approve,
        refuse
    }
    private String frequence;
}
