package com.tontine.repository;

import com.tontine.entities.Tontine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TontineRepository extends JpaRepository<Tontine, Integer> {
}
