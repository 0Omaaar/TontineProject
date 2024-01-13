package com.tontine.entities;

import jakarta.persistence.*;
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
public class GroupeUser {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String nomGroupe;

    @OneToMany(mappedBy = "groupeUser")
    private Collection<DemandeJointure> demandeJointures = new ArrayList<>();

    @OneToMany(mappedBy = "groupeUser")
    private Collection<User_GroupeUser> userGroupeUsers = new ArrayList<>();

    @OneToOne(mappedBy = "groupeUser")
    private MembreTontine membreTontine;

    public GroupeUser(String nomGroupe) {
        this.nomGroupe=nomGroupe;
    }
}
