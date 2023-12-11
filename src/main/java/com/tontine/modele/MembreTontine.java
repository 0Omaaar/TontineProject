package com.tontine.modele;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class MembreTontine {
    @Id
    @GeneratedValue
    private int id;
    @NotBlank
    @Column(nullable = false)
    private Date dateadhesion;
    @OneToOne
    private Utilisateur utilisateur;


}