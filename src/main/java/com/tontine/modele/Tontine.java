package com.tontine.modele;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
public class Tontine {
    @Id
    @GeneratedValue
    private int id;
    private Date dateApproveTontine;
//    private enum {
//
//    }
    private int tourCourant;
}
