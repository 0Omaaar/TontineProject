package com.tontine.service;

import com.tontine.entities.DemandeJointure;
import com.tontine.repository.DemandeJointureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public interface DemandeJointureService {
    public DemandeJointure saveDemandeJointure(DemandeJointure demandeJointure);
}