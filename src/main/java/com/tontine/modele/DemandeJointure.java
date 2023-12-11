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
public class DemandeJointure {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Date date;

    private enum statut{
        APPROUVE,
        REFUSE
    }

    @Column(nullable = true)
    private Date date_approuve;
}
