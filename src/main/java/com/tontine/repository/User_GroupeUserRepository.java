package com.tontine.repository;

import com.tontine.entities.User_GroupeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User_GroupeUserRepository extends JpaRepository<User_GroupeUser, Integer> {
}
