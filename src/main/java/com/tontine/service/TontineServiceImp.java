package com.tontine.service;

import com.tontine.entities.Tontine;
import com.tontine.repository.TontineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TontineServiceImp implements TontineService {

    @Autowired
    private TontineRepository tontineRepository;

    @Override
    public List<Tontine> findAll(){
        return tontineRepository.findAll();
    }
}
