package com.tontine.controller;

import com.tontine.entities.DemandeTontineEntite;
import com.tontine.entities.User;
import com.tontine.repository.DemanderTontineRepository;
import com.tontine.repository.UserRepository;
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


    @PostMapping("/tontine/demander-tontine")
    public ModelAndView creerTontine(@ModelAttribute("DemandeTontineEntite") DemandeTontineEntite demandeTontineEntine,
                                     RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();
        try{
            demanderTontineRepository.save(demandeTontineEntine);
            redirectAttributes.addFlashAttribute("successMessage", "Demande De Création de tontine envoyée avec succès");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une Erreur Est Survenue Lors De la Demande de Création de Tontine.");
        }
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
