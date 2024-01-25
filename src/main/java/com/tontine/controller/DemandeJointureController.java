package com.tontine.controller;

import com.tontine.entities.DemandeJointure;
import com.tontine.entities.GroupeUser;
import com.tontine.entities.User;
import com.tontine.entities.User_GroupeUser;
import com.tontine.repository.GroupeUserRepository;
import com.tontine.repository.User_GroupeUserRepository;
import com.tontine.service.DemandeJointureService;
import com.tontine.service.DemandeJointureServiceImp;
import com.tontine.service.EmailService;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@RestController
public class DemandeJointureController {

    @Autowired
    private DemandeJointureService demandeJointureService;

    @Autowired
    private GroupeUserRepository groupeUserRepository;

    @Autowired
    private User_GroupeUserRepository userGroupeUserRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/demander-jointure")
    public ModelAndView saveDemandeJointure(@ModelAttribute("DemandeJointure") DemandeJointure demandeJointure,
//                                            @RequestParam(name = "participationType", required = false) String participationType,
                                            @RequestParam(name = "cotisation", required = false) Float cotisation,
                                            @RequestParam(name = "selectedGroupId", required = false) Integer selectedGroupId,
                                            RedirectAttributes redirectAttributes){


        try{
            if (demandeJointure.getParticipationType().equals("EN_GROUPE")) {

                Optional<GroupeUser> groupeUserOptional = groupeUserRepository.findById(selectedGroupId);
                GroupeUser groupeUser = groupeUserOptional.orElse(null);
                float somme = demandeJointure.getCotisation();
                List<User_GroupeUser> userGroupeUsers = (List<User_GroupeUser>) userGroupeUserRepository.findUser_GroupeUsersByGroupeUser_Id((int)groupeUser.getId());
                for(User_GroupeUser userGroupUser : userGroupeUsers ){
                    somme += userGroupUser.getPourcentageCotisation();
                }
                if(somme > 1){
                    System.out.println(somme);
                    redirectAttributes.addFlashAttribute("dangerMessage", "Vous Pouvez pas Joindre ce groupe.");
                    return new ModelAndView("redirect:/");
                }
                demandeJointure.setGroupeUser(groupeUser);

            }

            Date date = new Date(); // Assuming this is the date you want to set
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            demandeJointure.setDate(localDate);
            demandeJointure.setStatut(DemandeJointure.Statut.EN_ATTENTE);
            demandeJointureService.saveDemandeJointure(demandeJointure);


            redirectAttributes.addFlashAttribute("successMessage", "Demande De Jointure Envoyée avec succès");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une Erreur Est Survenue Lors de la Demande de Jointure .");
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/");


        emailService.sendSimpleMessage("elkhotriomarpro@gmail.com", "Test Subject", "Test Text");

        return modelAndView;
    }



}
