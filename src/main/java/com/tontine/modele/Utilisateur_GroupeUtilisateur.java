package com.tontine.modele;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur_GroupeUtilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    private GroupeUtilisateur groupeUtilisateur;

    @Column(length = 10, nullable = true) //cotisation se fait par 0.1, 0.2 ...
    private Float pourcentageCotisation;

}
