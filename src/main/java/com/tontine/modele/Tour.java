package com.tontine.modele;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Tour {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    private Date dateTour;

    private int nbrTour;
}
