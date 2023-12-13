package com.tontine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String username;

    @NotBlank
    @Column(length = 100, nullable = false, unique = true)
    @Email
    private String email;

    @NotBlank
    @Column(nullable = false)
    //missing validation
    private String password;
    // a verifier @Column()

    @ManyToMany
    private Collection<Role> roles;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    MembreTontine membreTontine;


    @ManyToMany
    @JoinTable(
            name = "Utilisateur_GroupeUtilisateur",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "groupeUtilisateur_id")
    )
    private List<GroupeUtilisateur> groupeUtilisateurs;

}
