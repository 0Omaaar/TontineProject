package com.tontine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DemandeTontineEntite extends Demandetontine {

//    @Id
//    @GeneratedValue( strategy = GenerationType.IDENTITY)

    @ManyToOne
    private User user;


}
