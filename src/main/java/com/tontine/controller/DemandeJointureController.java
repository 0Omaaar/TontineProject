package com.tontine.controller;

import com.tontine.entities.DemandeJointure;
import com.tontine.service.DemandeJointureService;
import com.tontine.service.DemandeJointureServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class DemandeJointureController {

    @Autowired
    private DemandeJointureService demandeJointureService;

    @PostMapping("/demander-jointure")
    public DemandeJointure saveDemandeJointure(@ModelAttribute("DemandeJointure") DemandeJointure demandeJointure){
        demandeJointure.setDate(new Date());
        demandeJointure.setStatut(DemandeJointure.Statut.EN_ATTENTE);
        return demandeJointureService.saveDemandeJointure(demandeJointure);
    }

}
