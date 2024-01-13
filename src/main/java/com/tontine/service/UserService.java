package com.tontine.service;

import com.tontine.entities.User;
import com.tontine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUsr(User user){
        return userRepository.save(user);
    }
}
