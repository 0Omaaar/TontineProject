package com.tontine.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class Utilisateur {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String nomComplet;

    @NotBlank
    @Column(length = 15, nullable = false, unique = true)
    private String cin;

    @NotBlank
    @Column(length = 100, nullable = false, unique = true)
    @Email
    private String email;

    @NotBlank
    @Column(nullable = false)
    //missing validation
    private String motDePasse;
    // a verifier @Column()

    @NotBlank
    @Column(length = 10, nullable = false, unique = true)
    private String numTele;


    public enum Role {
        ADMIN,
        USER
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "utilisateur", fetch = FetchType.LAZY)
    MembreTontine membreTontine;

    @OneToMany(mappedBy = "utilisateur", fetch = FetchType.LAZY)
    private List<DemandeJointure> demandeJointures;

    @OneToMany(mappedBy = "utilisateur")
    private Collection<Utilisateur_GroupeUtilisateur> utilisateur_groupeUtilisateurs;


//    @ManyToMany
//            (mappedBy = "utilisateurs", fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "GroupeOfUser",
//            joinColumns = @JoinColumn(name = "utilisateurId"),
//            inverseJoinColumns = @JoinColumn(name = "groupeId")
//    )
//    private List<GroupeUtilisateur> groupeUtilisateurs;

    @OneToMany(mappedBy = "utilisateur")
    private List<DemandeTontineEntite> demandeTontineEntites;

}
