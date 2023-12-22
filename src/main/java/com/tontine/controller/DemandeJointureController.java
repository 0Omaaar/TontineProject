package com.tontine.controller;

import com.tontine.entities.DemandeJointure;
import com.tontine.service.DemandeJointureServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemandeJointureController {

    @Autowired
    private DemandeJointureServiceImp demandeJointureServiceImp;

    @PostMapping("/demande-jointure")
    public DemandeJointure saveDemandeJointure(@RequestBody DemandeJointure demandeJointure){
        return demandeJointureServiceImp.saveDemandeJointure(demandeJointure);
    }

}
