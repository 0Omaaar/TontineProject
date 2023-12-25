package com.tontine.repository;

import com.tontine.entities.DemandeTontineEntite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemanderTontineRepository extends JpaRepository<DemandeTontineEntite, Integer> {
}
