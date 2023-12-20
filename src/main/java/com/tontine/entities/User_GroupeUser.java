package com.tontine.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User_GroupeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private GroupeUser groupeUser;

    @Column(length = 10, nullable = false) //cotisation se fait par 0.1, 0.2 ...
    private Float pourcentageCotisation;

}
