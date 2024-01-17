package com.tontine.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
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

    private LocalDate dateadhesion;


    private Boolean paye;


//    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    private User user;


    @OneToOne
    private GroupeUser groupeUser;

    @OneToOne(mappedBy = "membreTontine")
    private Tour tour;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Tontine> tontines = new ArrayList<>( );

}
