package com.tontine.repository;

import com.tontine.entities.GroupeUser;
import com.tontine.entities.User_GroupeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface User_GroupeUserRepository extends JpaRepository<User_GroupeUser, Integer> {
    public Collection<User_GroupeUser> findUser_GroupeUsersByGroupeUser_Id(int id);
}
