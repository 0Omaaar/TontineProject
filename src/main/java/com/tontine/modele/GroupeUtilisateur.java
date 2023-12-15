package com.tontine.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class GroupeUtilisateur {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

//    @Column(nullable = true)
//    private Float pourcentageCotisation;

    @OneToMany(mappedBy = "groupeUtilisateur")
    private Collection<Utilisateur_GroupeUtilisateur> utilisateurGroupeUtilisateurs;


//    @ManyToMany(fetch = FetchType.EAGER)
//    private List<Utilisateur> utilisateurs = new ArrayList<>();

    @OneToOne(mappedBy = "groupeUtilisateur")
    private MembreTontine membreTontine;
}
