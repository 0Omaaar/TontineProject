package com.tontine.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class Utilisateur {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String nomComplet;

    @NotBlank
    @Column(length = 100, nullable = false, unique = true)
    @Email
    private String email;

    @NotBlank
    @Column(nullable = false)
    //missing validation
    private String motDePasse;
    // a verifier @Column()


    private enum Role {
        ADMIN,
        USER
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "utilisateur", fetch = FetchType.LAZY)
    MembreTontine membreTontine;


    @OneToMany
    private GroupeUtilisateur groupeUtilisateur;

}
