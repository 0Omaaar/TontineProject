package com.tontine.repository;

import com.tontine.entities.Tontine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TontineRepository extends JpaRepository<Tontine, Integer> {

}