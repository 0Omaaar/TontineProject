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

            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            demandeJointure.setDate(localDate);
            demandeJointure.setStatut(DemandeJointure.Statut.EN_ATTENTE);
            demandeJointureService.saveDemandeJointure(demandeJointure);

            String message = "<p>Bonjour,</p>"
                    + "<p>Une nouvelle demande de jointure a été soumise pour la tontine : <strong>" + demandeJointure.getTontine().getNom() + "</strong>.</p>"
                    + "<p>Demandeur : " + demandeJointure.getUser().getNom_prenom() + "</p>"
                    + "<p>Date de la demande : " + demandeJointure.getDate() + "</p>"
                    + "<p>Veuillez traiter cette demande dès que possible.</p>"
                    + "Pour accéder à votre page de gestion des demandes, veuillez cliquer sur le lien ci-dessous:<br/>"
                    + "<a href=\"http://localhost:9090/demandesJointure\">Accéder à la page de gestion</a></p>";

            emailService.sendSimpleMessage("elkhotriomarpro@gmail.com", "Demande de Jointure - " + demandeJointure.getTontine().getNom(), message);

            redirectAttributes.addFlashAttribute("successMessage", "Demande De Jointure Envoyée avec succès");

        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une Erreur Est Survenue Lors de la Demande de Jointure .");
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/");



        return modelAndView;
    }



}
