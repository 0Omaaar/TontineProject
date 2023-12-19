package com.tontine.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String nom_prenom;

    @NotBlank
    @Column(length = 15, nullable = false, unique = true)
    private String cin;

    @Column(unique = true)
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Column(length = 10, nullable = false, unique = true)
    private String numTele;

    @NotBlank
    @Column(nullable = false)
    private String password;


    @Column(nullable = false)
    private String roles;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    MembreTontine membreTontine;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<DemandeJointure> demandeJointures;

    @OneToMany(mappedBy = "user")
    private Collection<User_GroupeUser> User_groupeUsers;


//    @ManyToMany
//            (mappedBy = "Users", fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "GroupeOfUser",
//            joinColumns = @JoinColumn(name = "UserId"),
//            inverseJoinColumns = @JoinColumn(name = "groupeId")
//    )
//    private List<GroupeUser> groupeUsers;

    @OneToMany(mappedBy = "user")
    private List<DemandeTontineEntite> demandeTontineEntites;
}
