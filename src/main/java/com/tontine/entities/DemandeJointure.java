package com.tontine.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;


@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
@Transactional
public class DemandeJointure {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDate date;

    private Float cotisation;

    private String participationType;


    public enum Statut {
        EN_ATTENTE,
        APPROUVE,
        REFUSE
    }
    @Enumerated(EnumType.STRING)
    private Statut statut;

    @Column(nullable = true)
    private LocalDate date_approuve;

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


