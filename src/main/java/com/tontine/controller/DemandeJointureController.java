package com.tontine.controller;

import com.tontine.entities.DemandeJointure;
import com.tontine.entities.GroupeUser;
import com.tontine.entities.User_GroupeUser;
import com.tontine.repository.GroupeUserRepository;
import com.tontine.repository.User_GroupeUserRepository;
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

    @Autowired
    private GroupeUserRepository groupeUserRepository;

    @Autowired
    private User_GroupeUserRepository userGroupeUserRepository;

    @PostMapping("/tontine/demander-jointure")
    public ModelAndView saveDemandeJointure(@ModelAttribute("DemandeJointure") DemandeJointure demandeJointure,
                                            @RequestParam(name = "participationType", required = false) String participationType,
                                            @RequestParam(name = "cotisation", required = false) Float cotisation) {
        demandeJointure.setDate(new Date());
        demandeJointure.setStatut(DemandeJointure.Statut.EN_ATTENTE);

        if ("EN_GROUPE".equals(participationType)) {

            GroupeUser groupeUser = new GroupeUser("g1");

            groupeUserRepository.save(groupeUser);


            demandeJointure.setGroupeUser(groupeUser);

            User_GroupeUser userGroupeUser = new User_GroupeUser();
            userGroupeUser.setUser(demandeJointure.getUser());
            userGroupeUser.setGroupeUser(demandeJointure.getGroupeUser());
            userGroupeUser.setPourcentageCotisation(cotisation);

            userGroupeUserRepository.save(userGroupeUser);
        }


        demandeJointureService.saveDemandeJointure(demandeJointure);

        ModelAndView modelAndView = new ModelAndView("redirect:/");
        return modelAndView;
    }



}
