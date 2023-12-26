package com.tontine.controller;

import com.tontine.entities.DemandeTontineEntite;
import com.tontine.entities.Demandetontine;
import com.tontine.service.DemandeTontineService;
import org.springframework.ui.Model;
import com.tontine.entities.Tontine;
import com.tontine.service.TontineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private TontineService tontineService;

    @Autowired
    private DemandeTontineService demandeTontineService;



    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView dashboard(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/dashboard");

        return modelAndView;
    }

    @GetMapping("/tontines")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView tontines(Model model){
        ModelAndView modelAndView = new ModelAndView();

        List<Tontine> tontines = tontineService.findAll();
        model.addAttribute("tontines", tontines);

        modelAndView.setViewName("admin/tontines");
        return modelAndView;
    }

    @GetMapping("/demandesTontine")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView demandesTontines(Model model){
        ModelAndView modelAndView = new ModelAndView();
        List<DemandeTontineEntite> demandesTontines = demandeTontineService.findAll();
        model.addAttribute("demandes", demandesTontines);
        modelAndView.setViewName("admin/demandesTontines");
        return modelAndView;
    }

    @GetMapping("/accepterDemande/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView accepterDemande(@PathVariable int id){
        ModelAndView modelAndView = new ModelAndView();

        DemandeTontineEntite demandeTontineEntite = demandeTontineService.findById(id);
        demandeTontineEntite.setStatutDemande(Demandetontine.StatutDemande.APPROUVE);
        demandeTontineService.save(demandeTontineEntite);

        Tontine tontine = new Tontine();
        tontine.setNom(demandeTontineEntite.getNom());
        tontine.setDateDebut(demandeTontineEntite.getDateDebut());
        tontine.setFrequence(demandeTontineEntite.getFrequence());
        tontine.setMaxMembre(demandeTontineEntite.getMaxMembre());
        tontine.setMontantPeriode(demandeTontineEntite.getMontantPeriode());
        tontine.setTypeOrdre(demandeTontineEntite.getTypeOrdre());
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        tontine.setDateApprouveTontine(localDate);
        tontine.setStatutTontine(Tontine.StatutTontine.EN_ATTENTE);
        tontine.setTourCourant(0);
        tontineService.save(tontine);

        modelAndView.setViewName("redirect:/demandesTontine");
        return modelAndView;
    }


    @GetMapping("/refuserDemande/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView refuserDemande(@PathVariable int id){

        DemandeTontineEntite demandeTontineEntite = demandeTontineService.findById(id);
        demandeTontineEntite.setStatutDemande(Demandetontine.StatutDemande.REFUSE);
        demandeTontineService.save(demandeTontineEntite);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/demandesTontine");

        return modelAndView;
    }


    @GetMapping("/supprimer-tontine-{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView supprimerTontine(@PathVariable int id){
        tontineService.deleteById(id);

        // ajouter traitment de suppression des demandes jointures de cette tontine

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/tontines");
        return modelAndView;
    }

}
