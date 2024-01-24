package com.tontine.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.ArrayList;
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
    private LocalDate dateApprouveTontine;

    public enum StatutTontine {
        EN_ATTENTE,
        EN_COURS,
        TERMINE
    }

    @Enumerated(EnumType.STRING)
    public StatutTontine statutTontine;

    @Column(nullable = true)
    private int tourCourant;

//    @JsonIgnore
    @ManyToMany(mappedBy = "tontines", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Collection<MembreTontine> membreTontines = new ArrayList<>();


    @OneToMany(mappedBy = "tontine", cascade = CascadeType.REMOVE)
    private Collection<DemandeJointure> demandeJointures = new ArrayList<>();

    @OneToMany(mappedBy = "tontine", cascade = CascadeType.REMOVE)
    private List<Tour> tourList = new ArrayList<>();

}
