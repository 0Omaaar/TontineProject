package com.tontine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Entity
public class MembreTontine {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(nullable = false)
    private LocalDate dateadhesion;

    @OneToOne
    private User user;

    @OneToOne
    private GroupeUser groupeUser;

    @OneToOne(mappedBy = "membreTontine")
    private Tour tour;

    @ManyToMany
    private Collection<Tontine> tontines;

}
