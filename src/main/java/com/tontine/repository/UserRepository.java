package com.tontine.repository;


import com.tontine.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select * from user where email = ?1", nativeQuery = true)
    Optional<User> findByEmail(String email);
}
