package com.tontine.service;

import com.tontine.entities.Tontine;
import com.tontine.repository.TontineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
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

    @Override
    public void deleteById(Integer id) {
        tontineRepository.deleteById(id);
    }

    @Override
    public Tontine save(Tontine tontine) {
        return tontineRepository.save(tontine);
    }
}
