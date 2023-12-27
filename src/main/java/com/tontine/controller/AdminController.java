package com.tontine.controller;

import com.tontine.entities.DemandeJointure;
import com.tontine.entities.DemandeTontineEntite;
import com.tontine.entities.Demandetontine;
import com.tontine.service.DemandeJointureService;
import com.tontine.service.DemandeTontineService;
import org.springframework.boot.Banner;
import org.springframework.ui.Model;
import com.tontine.entities.Tontine;
import com.tontine.service.TontineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AdminController {

    @Autowired
    private TontineService tontineService;

    @Autowired
    private DemandeTontineService demandeTontineService;

    @Autowired
    private DemandeJointureService demandeJointureService;



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

    @GetMapping("ajouterTontine")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView ajouterTontine(Model model){
        Tontine tontine = new Tontine();
        model.addAttribute("tontine", tontine);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/ajouterTontine");
        return modelAndView;
    }

    @PostMapping("/saveTontine")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView saveTontine(@ModelAttribute("Tontine") Tontine tontine){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/tontines");

        tontineService.save(tontine);

        return modelAndView;
    }

    @GetMapping("/demandesJointure")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView demandesJointure(Model model){
        ModelAndView modelAndView = new ModelAndView();
        List<DemandeJointure> demandeJointures = demandeJointureService.findAll();

        model.addAttribute("demandes", demandeJointures);
        modelAndView.setViewName("admin/demandesJointure");
        return modelAndView;
    }

    @GetMapping("/refuserDemandeJointure/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView refuserDemandeJointure(@PathVariable("id") int id){
        ModelAndView modelAndView = new ModelAndView();

        DemandeJointure demandeJointure = demandeJointureService.findById(id);

        if(demandeJointure != null){
            demandeJointure.setStatut(DemandeJointure.Statut.REFUSE);
            demandeJointureService.saveDemandeJointure(demandeJointure);
        }

        modelAndView.setViewName("redirect:/demandesJointure");
        return modelAndView;
    }

    @GetMapping("/demandesTontine")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView demandesTontines(Model model){
        ModelAndView modelAndView = new ModelAndView();
        List<DemandeTontineEntite> demandesTontines = demandeTontineService.findAll();

        List<DemandeTontineEntite> demandesEnAttente = demandesTontines.stream()
                        .filter(demandeTontineEntite ->
                                demandeTontineEntite.getStatutDemande() == Demandetontine.StatutDemande.EN_ATTENTE)
                                .collect(Collectors.toList());


        model.addAttribute("demandes", demandesEnAttente);
        modelAndView.setViewName("admin/demandesTontines");
        return modelAndView;
    }

    @GetMapping("/demandesTontineRefusees")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView demandesTontineRefusees(Model model){
        ModelAndView modelAndView = new ModelAndView();
        List<DemandeTontineEntite> demandesTontines = demandeTontineService.findAll();

        List<DemandeTontineEntite> demandesRefusees = demandesTontines.stream()
                .filter(demandeTontineEntite ->
                        demandeTontineEntite.getStatutDemande() == Demandetontine.StatutDemande.REFUSE)
                .collect(Collectors.toList());


        model.addAttribute("demandes", demandesRefusees);
        modelAndView.setViewName("admin/demandesTontineRefusees");
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
