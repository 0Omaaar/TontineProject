package com.tontine.service;

import com.tontine.entities.DemandeJointure;
import com.tontine.repository.DemandeJointureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemandeJointureServiceImp {
    @Autowired
    private DemandeJointureRepository demandeJointureRepository;
    public DemandeJointure saveDemandeJointure(DemandeJointure demandeJointure){
        return demandeJointureRepository.save(demandeJointure);
    }

}
