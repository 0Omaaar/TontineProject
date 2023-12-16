package com.tontine.repositories;

import com.tontine.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public interface UserRepository extends JpaRepository<User, Long> {
    public List<User> findAllById(Long id);
}
