package com.tontine.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class GroupeUtilisateur {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = true)
    private Float pourcentageCotisation;


    @OneToMany(mappedBy = "groupeUtilisateur")
    private List<Utilisateur> utilisateur;
}
