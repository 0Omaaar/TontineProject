package com.tontine.modele;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;


@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class Tontine extends Demandetontine{
    @Column(nullable = true)
    private Date dateApprouveTontine;

    private enum Statut {
        ENATTENTE,
        ENCOURS,
        TERMINE
    }

    @Enumerated(EnumType.STRING)
    private Statut statut;

    @Column(nullable = true)
    private int tourCourant;

}
