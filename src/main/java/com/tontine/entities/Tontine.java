package com.tontine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Collection;
import java.util.Date;
import java.util.List;


//@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Tontine extends Demandetontine{

    @Column(nullable = true)
    private Date dateApprouveTontine;

    private enum Statut {
        EN_ATTENTE,
        EN_COURS,
        TERMINE
    }

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @Column(nullable = true)
    private int tourCourant;

    @ManyToMany(mappedBy = "tontines", fetch = FetchType.EAGER)
    private Collection<MembreTontine> membreTontines;


    @OneToMany(mappedBy = "tontine")
//    @JoinTable(
//            name = "Tontine_Membre",
//            joinColumns = @JoinColumn(name = "tontineId"),
//            inverseJoinColumns = @JoinColumn(name = "membreId")
//    )
    private Collection<DemandeJointure> demandeJointures;



}
