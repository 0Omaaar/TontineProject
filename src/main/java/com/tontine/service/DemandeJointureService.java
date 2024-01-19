package com.tontine.service;

import com.tontine.entities.DemandeJointure;
import com.tontine.repository.DemandeJointureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DemandeJointureService {
    public DemandeJointure saveDemandeJointure(DemandeJointure demandeJointure);

    public List<DemandeJointure> findAll();

    public DemandeJointure findById(int id);

}
