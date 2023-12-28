package com.tontine.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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


//    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    private User user;


    @OneToOne(cascade = CascadeType.REMOVE)
    private GroupeUser groupeUser;

    @OneToOne(mappedBy = "membreTontine")
    private Tour tour;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<Tontine> tontines = new ArrayList<>();

}
