package com.tontine.service;

import com.tontine.entities.DemandeJointure;
import com.tontine.repository.DemandeJointureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DemandeJointureServiceImp implements DemandeJointureService{



    @Autowired
    private DemandeJointureRepository demandeJointureRepository;

    @Override
    public DemandeJointure saveDemandeJointure(DemandeJointure demandeJointure){
        return demandeJointureRepository.save(demandeJointure);


    }

    @Override
    public List<DemandeJointure> findAll() {
        return demandeJointureRepository.findAll();
    }

    @Override
    public DemandeJointure findById(int id) {
        return demandeJointureRepository.findById(id).orElse(null);
    }

}
