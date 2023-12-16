package com.tontine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

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
    private long id;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String username;

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
    private String password;
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



    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    MembreTontine membreTontine;

    @OneToMany(mappedBy = "User", fetch = FetchType.LAZY)
    private List<DemandeJointure> demandeJointures;

    @OneToMany(mappedBy = "User")
    private Collection<User_GroupeUser> User_groupeUsers;


//    @ManyToMany
//            (mappedBy = "Users", fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "GroupeOfUser",
//            joinColumns = @JoinColumn(name = "UserId"),
//            inverseJoinColumns = @JoinColumn(name = "groupeId")
//    )
//    private List<GroupeUser> groupeUsers;

    @OneToMany(mappedBy = "User")
    private List<com.tontine.entities.DemandeTontineEntite> demandeTontineEntites;

}
