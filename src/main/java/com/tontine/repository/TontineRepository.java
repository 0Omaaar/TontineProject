package com.tontine.repository;

import com.tontine.entities.Tontine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TontineRepository extends JpaRepository<Tontine, Integer> {
    List<Tontine> findByNomContains(String nom);
}