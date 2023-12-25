package com.tontine.controller;

import com.tontine.entities.DemandeTontineEntite;
import com.tontine.repository.DemanderTontineRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CreerTontineController {

    private DemanderTontineRepository demanderTontineRepository;

    @GetMapping("/créer-tontine")
    public ModelAndView demanderTontine(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ff.html");
        return modelAndView;
    }

    @PostMapping("/tontine/demander-tontine")
    public ModelAndView creerTontine(@ModelAttribute("DemandeTontineEntite") DemandeTontineEntite demandeTontineEntite){
        ModelAndView modelAndView = new ModelAndView();
        demanderTontineRepository.save(demandeTontineEntite);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
