package com.tontine.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;


@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class DemandeJointure {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDate date;

    public enum ParticipationType {
        SEUL,
        EN_GROUPE_NEW,
        EN_GROUPE
    }
    @Enumerated(EnumType.STRING)
    private ParticipationType participationType;



    public enum Statut {
        EN_ATTENTE,
        APPROUVE,
        REFUSE
    }


    @Enumerated(EnumType.STRING)
    private Statut statut;

    @Column(nullable = true)
    private LocalDate date_approuve;

//    @ManyToOne
//    private MembreTontine membreTontine;


    @JsonIgnore
    @ManyToOne
    private GroupeUser groupeUser;

    @JsonIgnore
    @ManyToOne
    private Tontine tontine;




    @JsonIgnore
    @ManyToOne
    private User user;
}


