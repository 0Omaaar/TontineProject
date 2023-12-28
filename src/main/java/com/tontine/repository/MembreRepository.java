package com.tontine.repository;

import com.tontine.entities.MembreTontine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembreRepository extends JpaRepository<MembreTontine, Integer> {
}
