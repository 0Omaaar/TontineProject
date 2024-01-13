package com.tontine.service;

import com.tontine.entities.User_GroupeUser;
import com.tontine.repository.User_GroupeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public abstract class User_GroupeUserService {

    @Autowired
    private User_GroupeUserRepository userGroupeUserRepository;

    public User_GroupeUser saveUserGrUs(User_GroupeUser userGroupeUser) {
        return userGroupeUserRepository.save(userGroupeUser);
    }


}
