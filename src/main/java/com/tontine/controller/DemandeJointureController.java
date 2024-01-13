package com.tontine.controller;

import com.tontine.entities.DemandeJointure;
import com.tontine.entities.GroupeUser;
import com.tontine.entities.User;
import com.tontine.entities.User_GroupeUser;
import com.tontine.repository.GroupeUserRepository;
import com.tontine.repository.User_GroupeUserRepository;
import com.tontine.service.DemandeJointureService;
import com.tontine.service.DemandeJointureServiceImp;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@RestController
public class DemandeJointureController {

    @Autowired
    private DemandeJointureService demandeJointureService;

    @Autowired
    private GroupeUserRepository groupeUserRepository;

    @Autowired
    private User_GroupeUserRepository userGroupeUserRepository;

    @PostMapping("/demander-jointure")
    public ModelAndView saveDemandeJointure(@ModelAttribute("DemandeJointure") DemandeJointure demandeJointure,
//                                            @RequestParam(name = "participationType", required = false) String participationType,
                                            @RequestParam(name = "cotisation", required = false) Float cotisation,
                                            @RequestParam(name = "selectedGroupId") int selectedGroupId){


        if(demandeJointure.getParticipationType().equals("EN_GROUPE_NEW"))
        {

            int nbr =  (int)groupeUserRepository.count() + 1;
            String nomG = "Groupe" + nbr;
            GroupeUser groupeUser = new GroupeUser(nomG);
            groupeUserRepository.save(groupeUser);
            demandeJointure.setGroupeUser(groupeUser);
        } else if (demandeJointure.getParticipationType().equals("EN_GROUPE_NEW")) {
            Optional<GroupeUser> groupeUserOptional = groupeUserRepository.findById(selectedGroupId);
            GroupeUser groupeUser = groupeUserOptional.orElse(null);
            demandeJointure.setGroupeUser(groupeUser); // groupeId ne s'emregistre pas dans BD

        }


        Date date = new Date(); // Assuming this is the date you want to set
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        demandeJointure.setDate(localDate);
        demandeJointure.setStatut(DemandeJointure.Statut.EN_ATTENTE);
        demandeJointureService.saveDemandeJointure(demandeJointure);


        ModelAndView modelAndView = new ModelAndView("redirect:/");
        return modelAndView;
    }



}
