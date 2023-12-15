package com.tontine.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DemandeTontineEntite extends Demandetontine {

//    @Id
//    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private String frequence;

    @ManyToOne
    private Utilisateur utilisateur;


}
