package com.tontine.service;

import com.tontine.entities.Tontine;
import com.tontine.repository.TontineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public abstract class TontineServiceImp implements TontineService{

    @Autowired
    private TontineRepository tontineRepository;

    public Optional<Tontine> findTontineById(int id){
        return tontineRepository.findById(id);
    }


}
