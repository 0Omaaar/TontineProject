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

    @GetMapping("/créer-tontine")
    public ModelAndView demanderTontine(Authentication authentication,
                                        Model model){
        ModelAndView modelAndView = new ModelAndView();
        int user_id = 0;
        User authenticated_user = null;

        if(authentication != null){
            authenticated_user = userRepository.findByEmail(getLoggedInUserDetails().getUsername()).orElse(null);
        }

        if(authenticated_user != null){
            user_id = authenticated_user.getId();
        }

        model.addAttribute("user_id", user_id);

        modelAndView.setViewName("ff");
        return modelAndView;
    }

    @PostMapping("/tontine/demander-tontine")
    public ModelAndView creerTontine(@ModelAttribute("DemandeTontineEntite") DemandeTontineEntite demandeTontineEntine
                                    ){
        ModelAndView modelAndView = new ModelAndView();

        demanderTontineRepository.save(demandeTontineEntine);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
