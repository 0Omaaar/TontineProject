package com.tontine.controller;

import com.tontine.entities.*;
import com.tontine.repository.GroupeUserRepository;
import com.tontine.repository.UserRepository;
import com.tontine.repository.User_GroupeUserRepository;
import com.tontine.service.*;
import jakarta.validation.Valid;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
//@Validated
public class AdminController {

    @Autowired
    private User_GroupeUserRepository userGroupeUserRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DemandeTontineService demandeTontineService;

    @Autowired
    private DemandeJointureService demandeJointureService;


    @Autowired
    private TontineService tontineService;


    @Autowired
    private MembreService membreService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupeUserRepository groupeUserRepository;

    @Autowired
    private TourService tourService;


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

    @PostMapping("/changer-statut-tontine")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView changerStatut(Tontine tontine, @RequestParam int tontine_id, @RequestParam String statutTontine){
        Tontine findedTontine = tontineService.findById(tontine_id).orElse(null);
        findedTontine.setStatutTontine(Tontine.StatutTontine.valueOf(statutTontine));
        tontineService.save(findedTontine);
        return new ModelAndView("redirect:/tontines");
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
    public ModelAndView saveTontine(@ModelAttribute("Tontine") @Valid Tontine tontine, BindingResult bindingResult
                                                        , Model model, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()){
            modelAndView.addObject("errorMessage", "An error occurred during the creation of the Tontine. Please check the entered data.");
            modelAndView.setViewName("redirect:/ajouterTontine");
            System.out.println(bindingResult);
            return modelAndView;
        }

        tontineService.save(tontine);
        redirectAttributes.addFlashAttribute("successMessage", "Dekshi daz mezian");
        modelAndView.setViewName("redirect:/tontines");

        return modelAndView;
    }

    @GetMapping("/demandesJointure")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView demandesJointure(Model model){
        ModelAndView modelAndView = new ModelAndView();
        List<DemandeJointure> demandeJointures = demandeJointureService.findAll();

        List<DemandeJointure> demandesEnAttentes = demandeJointures.stream()
                .filter(demande -> demande.getStatut() == DemandeJointure.Statut.EN_ATTENTE)
                        .collect(Collectors.toList());

        model.addAttribute("demandes", demandesEnAttentes);
        modelAndView.setViewName("admin/demandesJointure");
        return modelAndView;
    }

    @GetMapping("/demandesJointureTraitees")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView demandesJointureTraitees(Model model){
        ModelAndView modelAndView = new ModelAndView();
        List<DemandeJointure> demandeJointures = demandeJointureService.findAll();

        List<DemandeJointure> demandes = demandeJointures.stream()
                .filter(demande -> demande.getStatut() != DemandeJointure.Statut.EN_ATTENTE)
                .collect(Collectors.toList());

        model.addAttribute("demandes", demandes);
        modelAndView.setViewName("admin/demandesJointureTraitees");
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

    @GetMapping("/accepterDemandeJointure/{id}")
    @Transactional
    public ModelAndView saveMembre(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView();
        DemandeJointure demandeJointure = demandeJointureService.findById(id);
        Tontine tontine = demandeJointure.getTontine();
        if(demandeJointure.getParticipationType().equals("EN_GROUPE_NEW"))
        {
            // Create and set up MembreTontine
            MembreTontine membreTontine = new MembreTontine();
            membreTontine.setDateadhesion(LocalDate.now());
            membreTontine.setUser(demandeJointure.getUser());
            membreTontine.getTontines().add(tontine);

            // Create and set up GroupeUser
            int nbr = (int) groupeUserRepository.count();
            String nomG = "Groupe" + nbr;
            GroupeUser groupeUser = new GroupeUser(nomG);
            groupeUser.setMembreTontine(membreTontine);

            // Create and set up User_GroupeUser
            User_GroupeUser userGroupeUser = new User_GroupeUser();
            userGroupeUser.setGroupeUser(groupeUser);
            userGroupeUser.setUser(demandeJointure.getUser());
            userGroupeUser.setPourcentageCotisation(demandeJointure.getCotisation());

            // Set relationships bidirectionally
            membreTontine.setGroupeUser(groupeUser);
            demandeJointure.setGroupeUser(groupeUser);
            groupeUser.getUserGroupeUsers().add(userGroupeUser);

            // Save entities in the correct order
            userGroupeUserRepository.save(userGroupeUser);
            userService.saveUsr(demandeJointure.getUser());
            groupeUserRepository.save(groupeUser);
            membreService.save(membreTontine);
            tontineService.save(tontine);

            demandeJointure.setStatut(DemandeJointure.Statut.APPROUVE);
            demandeJointureService.saveDemandeJointure(demandeJointure);

            //function for creating tour for member
            createTour(demandeJointure, membreTontine);
        } else if (demandeJointure.getParticipationType().equals("EN_GROUPE")) {
            User_GroupeUser userGroupeUser = new User_GroupeUser();
            userGroupeUser.setGroupeUser(demandeJointure.getGroupeUser());
            userGroupeUser.setUser(demandeJointure.getUser());
            userGroupeUser.setPourcentageCotisation(demandeJointure.getCotisation());
            demandeJointure.setStatut(DemandeJointure.Statut.APPROUVE);
            demandeJointureService.saveDemandeJointure(demandeJointure);
            userGroupeUserRepository.save(userGroupeUser);
        }
        else {
            MembreTontine membreTontine = new MembreTontine();
            tontine.getMembreTontines().add(membreTontine);

            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            membreTontine.setDateadhesion(localDate);
            membreTontine.setUser(demandeJointure.getUser());
            membreTontine.getTontines().add(tontine);
            demandeJointure.setStatut(DemandeJointure.Statut.APPROUVE);
            membreService.save(membreTontine);
            tontineService.save(tontine);

            //function to create the tour
            createTour(demandeJointure, membreTontine);
        }
        // Set the view and return
        modelAndView.setViewName("redirect:/dashboard");
        return modelAndView;
    }

    private void createTour(DemandeJointure demandeJointure, MembreTontine membreTontine){
        if(demandeJointure.getTontine().getTypeOrdre() == Demandetontine.TypeOrdre.ORDER){
            Tour tour = new Tour();
            tour.setMembreTontine(membreTontine);
            tour.setNbrTour(demandeJointure.getTontine().getMembreTontines().size() );
            int nbrJour;
            if(demandeJointure.getTontine().getFrequence() == Demandetontine.Frequence.HEBDOMADAIRE)
            {
                nbrJour = 7;
            } else if (demandeJointure.getTontine().getFrequence() == Demandetontine.Frequence.MENTUEL) {
                nbrJour = 30;
            }
            else {
                nbrJour = 90;
            }
            LocalDate dateTour = demandeJointure.getTontine().getDateDebut().plusDays((long) nbrJour * demandeJointure.getTontine().getMembreTontines().size() );
            tour.setDateTour(dateTour);

            tourService.saveTour(tour);
        }
    }

    @GetMapping("afficherMembres")
    public ModelAndView afficherMembres(@RequestParam(name = "tontineId") int tontineId, Model model){

        Tontine tontine = tontineService.findById(tontineId).orElse(null);

        if(tontine != null){
             List<MembreTontine> membreTontines =  (List<MembreTontine>) tontine.getMembreTontines();
             model.addAttribute("membresTontine", membreTontines);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/membresTontine");
//        modelAndView.setViewName("membres");
        return modelAndView;
    }



    @GetMapping("/utilisateurs")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView utilisateurs(Model model){
        model.addAttribute("users", userRepository.findAll());

        return new ModelAndView("admin/utilisateurs/index");
    }


    @GetMapping("supprimer-user-{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView supprimerUser(@PathVariable(name = "id") int id){
        User user = userRepository.findById(id).orElse(null);
        userRepository.delete(user);
        return new ModelAndView("redirect:/utilisateurs");
    }

    @PostMapping("/modifier-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView modifierUser(@ModelAttribute(name = "user") User user, @RequestParam(name = "id") int user_id){
        User userF = userRepository.findById(user_id).orElse(null);

        if (userF != null) {
            userF.setEmail(user.getEmail());
            userF.setCin(user.getCin());
            userF.setNumTele(user.getNumTele());
            userF.setPassword(user.getPassword());
            userF.setNom_prenom(user.getNom_prenom());

            userRepository.save(userF);
        }

        return new ModelAndView("redirect:/utilisateurs");
    }

    @PostMapping("/ajouter-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView ajouterUser(@ModelAttribute(name = "user") User user){
        userRepository.save(user);

        return new ModelAndView("redirect:/utilisateurs");
    }

    @GetMapping("/groupes")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView groupes(Model model){
        model.addAttribute("groupes", groupeUserRepository.findAll());

        return new ModelAndView("admin/utilisateurs/groupes/groupes");
    }



}


