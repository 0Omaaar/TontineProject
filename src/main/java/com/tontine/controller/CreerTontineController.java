package com.tontine.controller;

import com.tontine.entities.DemandeTontineEntite;
import com.tontine.entities.User;
import com.tontine.repository.DemanderTontineRepository;
import com.tontine.repository.UserRepository;
import com.tontine.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class CreerTontineController {

    public UserDetails getLoggedInUserDetails(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails){
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

    @Autowired
    private DemanderTontineRepository demanderTontineRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    @PostMapping("/tontine/demander-tontine")
    public ModelAndView creerTontine(@ModelAttribute("DemandeTontineEntite") DemandeTontineEntite demandeTontineEntine,
                                     RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();
        try{
            demanderTontineRepository.save(demandeTontineEntine);

            String message = "<p>Bonjour,</p>"
                    + "<p>Une nouvelle demande de création de tontine a été soumise avec le Nom : <strong>" + demandeTontineEntine.getNom() + "</strong>.</p>"
                    + "<p>Demandeur : " + demandeTontineEntine.getUser().getNom_prenom() + "</p>"
                    + "<p>Veuillez traiter cette demande dès que possible.</p>"
                    + "Pour accéder à votre page de gestion des demandes, veuillez cliquer sur le lien ci-dessous:<br/>"
                    + "<a href=\"http://localhost:9090/demandesTontine\">Accéder à la page de gestion</a></p>";

            emailService.sendSimpleMessage("elkhotriomarpro@gmail.com", "Demande de Jointure - " + demandeTontineEntine.getNom(), message);

            redirectAttributes.addFlashAttribute("successMessage", "Demande De Création de tontine envoyée avec succès");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une Erreur Est Survenue Lors De la Demande de Création de Tontine.");
        }
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
