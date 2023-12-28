package com.tontine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


//@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Tontine extends Demandetontine{

    @Column(nullable = true)
    private LocalDate dateApprouveTontine;

    public enum StatutTontine {
        EN_ATTENTE,
        EN_COURS,
        TERMINE
    }

    @Enumerated(EnumType.STRING)
    private StatutTontine statutTontine;

    @Column(nullable = true)
    private int tourCourant;

    @ManyToMany(mappedBy = "tontines", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<MembreTontine> membreTontines = new ArrayList<>();


    @OneToMany(mappedBy = "tontine")
    private Collection<DemandeJointure> demandeJointures = new ArrayList<>();

}
