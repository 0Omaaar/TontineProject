package com.tontine.repository;

import com.tontine.entities.DemandeJointure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DemandeJointureRepository extends JpaRepository<com.tontine.entities.DemandeJointure, Integer> {

//    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
//            "FROM DemandeJointure d " +
//            "JOIN d.user u " +
//            "JOIN d.tontine t " +
//            "WHERE d.statut = 'EN_ATTENTE' AND u.id = :user_id AND t.id = :tontine_id")
//    public boolean findByTontineUser(int user_id, int tontine_id);


}
