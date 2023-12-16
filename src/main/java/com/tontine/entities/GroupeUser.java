package com.tontine.entities;

import jakarta.persistence.*;
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
public class GroupeUser {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

//    @Column(nullable = true)
//    private Float pourcentageCotisation;

    @OneToMany(mappedBy = "groupeUser")
    private Collection<User_GroupeUser> UserGroupeUsers;



//    @ManyToMany(fetch = FetchType.EAGER)
//    private List<User> Users = new ArrayList<>();

    @OneToOne(mappedBy = "groupeUser")
    private MembreTontine membreTontine;
    @ManyToMany(mappedBy = "groupeUsers")
    private List<User> users;
}
