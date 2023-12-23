package com.tontine.controller;

import com.tontine.entities.DemandeJointure;
import com.tontine.entities.GroupeUser;
import com.tontine.service.DemandeJointureService;
import com.tontine.service.DemandeJointureServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;

@RestController
public class DemandeJointureController {

    @Autowired
    private DemandeJointureService demandeJointureService;

    @PostMapping("/demander-jointure")
    public ModelAndView saveDemandeJointure(@ModelAttribute("DemandeJointure") DemandeJointure demandeJointure,
                                            @RequestParam(name = "participationType", required = false) String participationType) {

        ModelAndView modelAndView = new ModelAndView("redirect:/");

        demandeJointure.setDate(new Date());
        demandeJointure.setStatut(DemandeJointure.Statut.EN_ATTENTE);

        if("EN_GROUPE".equals(participationType)){

            String nomGroupe = demandeJointure.getTontine().getNom() + "G1";
            demandeJointure.setGroupeUser(new GroupeUser(nomGroupe));
            demandeJointure.getUser().setUser_groupeUsers(new ArrayList<>());


        }

        DemandeJointure savedDemande = demandeJointureService.saveDemandeJointure(demandeJointure);

        if (savedDemande != null) {
            modelAndView.addObject("successMessage", "Your success message here");
        } else {
            modelAndView.addObject("errorMessage", "Failed to save demande. Please try again.");
        }

        return modelAndView;
    }


}
