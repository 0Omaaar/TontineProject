package com.tontine.controller;

import com.tontine.entities.*;
import com.tontine.repository.GroupeUserRepository;
import com.tontine.repository.UserRepository;
import com.tontine.repository.User_GroupeUserRepository;
import com.tontine.service.*;
import jakarta.validation.Valid;
import org.springframework.cglib.core.Local;
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
import java.util.*;
import java.util.stream.Collectors;

//import static com.tontine.controller.AdminController.RandomNumberGenerator.generateUniqueRandomNumber;

@RestController
//@Validated
public class AdminController {

//    @Autowired
//    private User_GroupeUserRepository userGroupeUserRepository;
//
//    @Autowired
//    private UserService userService;

    @Autowired
    private DemandeTontineService demandeTontineService;

    @Autowired
    private DemandeJointureService demandeJointureService;


    @Autowired
    private TontineService tontineService;


    @Autowired
    private MembreServiceImp membreServiceImp;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupeUserRepository groupeUserRepository;

//    @Autowired
//    private TourService tourService;


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
        Collections.reverse(tontines);

        model.addAttribute("tontines", tontines);

        modelAndView.setViewName("admin/tontines");
        return modelAndView;
    }

    @PostMapping("/changer-statut-tontine")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView changerStatut(Tontine tontine, @RequestParam int tontine_id, @RequestParam String statutTontine,
                                      RedirectAttributes redirectAttributes){
        try{
            Tontine findedTontine = tontineService.findById(tontine_id);
            if(!findedTontine.getStatutTontine().equals(Tontine.StatutTontine.EN_ATTENTE)){
                redirectAttributes.addFlashAttribute("dangerMessage", "Vous Pouvez pas modifier le statut de la tontine");
                return new ModelAndView("redirect:/tontines");
            }
            findedTontine.setStatutTontine(Tontine.StatutTontine.valueOf(statutTontine));
            tontineService.save(findedTontine);
            redirectAttributes.addFlashAttribute("successMessage", "Le Statut est Modifié avec succès.");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Erreur Survenue Lors de la modification de statut.");
        }
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
            redirectAttributes.addFlashAttribute("dangerMessage", "An error occurred during the creation of the Tontine. Please check the entered data.");
            modelAndView.setViewName("redirect:/ajouterTontine");
            return modelAndView;
        }

        int nbrJour;
        if(tontine.getFrequence() == Demandetontine.Frequence.HEBDOMADAIRE)
        {
            nbrJour = 7;
        } else if (tontine.getFrequence() == Demandetontine.Frequence.MENTUEL) {
            nbrJour = 30;
        }
        else {
            nbrJour = 90;
        }


        tontine.setDateFin(tontine.getDateDebut().plusDays((long) nbrJour * tontine.getMaxMembre()));
        tontineService.save(tontine);
        redirectAttributes.addFlashAttribute("successMessage", "Tontine Crée avec succès");
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

        Collections.reverse(demandesEnAttentes);

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


        Collections.reverse(demandes);
        model.addAttribute("demandes", demandes);
        modelAndView.setViewName("admin/demandesJointureTraitees");
        return modelAndView;
    }

    @GetMapping("/refuserDemandeJointure/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView refuserDemandeJointure(@PathVariable("id") int id, RedirectAttributes redirectAttributes){
        try {
            DemandeJointure demandeJointure = demandeJointureService.findById(id);

            if(demandeJointure != null){
                demandeJointure.setStatut(DemandeJointure.Statut.REFUSE);
                demandeJointureService.saveDemandeJointure(demandeJointure);

            }

            redirectAttributes.addFlashAttribute("successMessage", "La demande a été refusée avec succès");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une Erreur est survenue lors de refus de Jointure");
        }

        ModelAndView modelAndView = new ModelAndView("redirect:/demandesJointure");
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


        Collections.reverse(demandesEnAttente);
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


        Collections.reverse(demandesRefusees);
        model.addAttribute("demandes", demandesRefusees);
        modelAndView.setViewName("admin/demandesTontineRefusees");
        return modelAndView;
    }

    @GetMapping("/accepterDemande/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public ModelAndView accepterDemande(@PathVariable int id, RedirectAttributes redirectAttributes){
        ModelAndView modelAndView = new ModelAndView();

        try{
            DemandeTontineEntite demandeTontineEntite = demandeTontineService.findById(id);
            demandeTontineEntite.setStatutDemande(Demandetontine.StatutDemande.APPROUVE);
            demandeTontineService.save(demandeTontineEntite);

            Tontine tontine = new Tontine();
            tontine.setNom(demandeTontineEntite.getNom());
            tontine.setDateDebut(demandeTontineEntite.getDateDebut());


            int nbrJour;
            if(demandeTontineEntite.getFrequence() == Demandetontine.Frequence.HEBDOMADAIRE)
            {
                nbrJour = 7;
            } else if (demandeTontineEntite.getFrequence() == Demandetontine.Frequence.MENTUEL) {
                nbrJour = 30;
            }
            else {
                nbrJour = 90;
            }


            tontine.setDateFin(demandeTontineEntite.getDateDebut().plusDays((long) nbrJour * demandeTontineEntite.getMaxMembre()));
            tontine.setDateApprouveTontine(LocalDate.now());
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

            redirectAttributes.addFlashAttribute("successMessage", "La demande a été accepté avec succès");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une erreur est survenue lors de l'acceptation de la demande");
        }

        modelAndView.setViewName("redirect:/demandesTontine");
        return modelAndView;
    }


    @GetMapping("/refuserDemande/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView refuserDemande(@PathVariable int id, RedirectAttributes redirectAttributes){

        try{
            DemandeTontineEntite demandeTontineEntite = demandeTontineService.findById(id);
            demandeTontineEntite.setStatutDemande(Demandetontine.StatutDemande.REFUSE);
            demandeTontineService.save(demandeTontineEntite);

            redirectAttributes.addFlashAttribute("successMessage", "La demande a été refusée avec succès");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une erreur est survenue lors de refus de la demande");
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/demandesTontineRefusees");

        return modelAndView;
    }


    @GetMapping("/supprimer-tontine-{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView supprimerTontine(@PathVariable int id, RedirectAttributes redirectAttributes){
        try{
            tontineService.deleteById(id);

            // ajouter traitment de suppression des demandes jointures de cette tontine

            redirectAttributes.addFlashAttribute("successMessage", "Tontine Supprimée avec succès");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Erreur Survenue lors de la suppression de la tontine");
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/tontines");
        return modelAndView;
    }

    @GetMapping("/accepterDemandeJointure/{id}")
    @Transactional
    public ModelAndView saveMembre(@PathVariable int id, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
//        try{
//            DemandeJointure demandeJointure = demandeJointureService.findById(id);
//            Tontine tontine = demandeJointure.getTontine();
//            if(demandeJointure.getParticipationType().equals("EN_GROUPE_NEW"))
//            {
//                // Create and set up MembreTontine
//                MembreTontine membreTontine = new MembreTontine();
//                membreTontine.setDateadhesion(LocalDate.now());
//                membreTontine.setUser(demandeJointure.getUser());
//                membreTontine.getTontines().add(tontine);
//
//                // Create and set up GroupeUser
//                int nbr = (int) groupeUserRepository.count();
//                String nomG = "Groupe" + nbr;
//                GroupeUser groupeUser = new GroupeUser(nomG);
//                groupeUser.setMembreTontine(membreTontine);
//
//                // Create and set up User_GroupeUser
//                User_GroupeUser userGroupeUser = new User_GroupeUser();
//                userGroupeUser.setGroupeUser(groupeUser);
//                userGroupeUser.setUser(demandeJointure.getUser());
//                userGroupeUser.setPourcentageCotisation(demandeJointure.getCotisation());
//
//                // Set relationships bidirectionally
//                membreTontine.setGroupeUser(groupeUser);
//                demandeJointure.setGroupeUser(groupeUser);
//                groupeUser.getUserGroupeUsers().add(userGroupeUser);
//
//                // Save entities in the correct order
//                userService.saveUsr(demandeJointure.getUser());
//                groupeUserRepository.save(groupeUser);
//                userGroupeUserRepository.save(userGroupeUser);
//                membreServiceImp.saveMembre(membreTontine);
//                tontineService.save(tontine);
//
//                demandeJointure.setStatut(DemandeJointure.Statut.APPROUVE);
//                demandeJointureService.saveDemandeJointure(demandeJointure);
//
//                //function for creating tour for member
//                createTour(demandeJointure, membreTontine);
//            } else if (demandeJointure.getParticipationType().equals("EN_GROUPE")) {
//                User_GroupeUser userGroupeUser = new User_GroupeUser();
//                userGroupeUser.setGroupeUser(demandeJointure.getGroupeUser());
//                userGroupeUser.setUser(demandeJointure.getUser());
//                userGroupeUser.setPourcentageCotisation(demandeJointure.getCotisation());
//                demandeJointure.setStatut(DemandeJointure.Statut.APPROUVE);
//                demandeJointureService.saveDemandeJointure(demandeJointure);
//                userGroupeUserRepository.save(userGroupeUser);
//            }
//            else {
//                MembreTontine membreTontine = new MembreTontine();
//                tontine.getMembreTontines().add(membreTontine);
//
//                Date date = new Date();
//                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                membreTontine.setDateadhesion(localDate);
//                membreTontine.setUser(demandeJointure.getUser());
//                membreTontine.getTontines().add(tontine);
//                demandeJointure.setStatut(DemandeJointure.Statut.APPROUVE);
//                membreServiceImp.saveMembre(membreTontine);
//                tontineService.save(tontine);
//
//                //function to create the tour
//                createTour(demandeJointure, membreTontine);
//            }
//            redirectAttributes.addFlashAttribute("successMessage", "La demande de jointure a été accepté avec succès");
//        }catch(Exception e){
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("dangerMessage", "Une Erreur Survenue lors de l'acceptation de la demande");
//        }

        membreServiceImp.saveMembreALlCases(id, redirectAttributes);
        // Set the view and return
        modelAndView.setViewName("redirect:/dashboard");
        return modelAndView;
    }

//    public void createTour(DemandeJointure demandeJointure, MembreTontine membreTontine){
//        if(demandeJointure.getTontine().getTypeOrdre().equals(Demandetontine.TypeOrdre.ORDER)){
////            if(demandeJointure.getTontine().getMembreTontines() == null){
////                System.out.println("aloo");
////                tour.setNbrTour(1);
////            }else{
////                tour.setNbrTour();
////            }
//            int nbrJour;
//            if(demandeJointure.getTontine().getFrequence().equals( Demandetontine.Frequence.HEBDOMADAIRE))
//            {
//                nbrJour = 7;
//            } else if (demandeJointure.getTontine().getFrequence() == Demandetontine.Frequence.MENTUEL) {
//                nbrJour = 30;
//            }
//            else {
//                nbrJour = 90;
//            }
//
//            LocalDate dateTour = demandeJointure.getTontine().getDateDebut().plusDays((long) nbrJour * demandeJointure.getTontine().getMembreTontines().size());
//            int nbrTr = demandeJointure.getTontine().getTourList().size() + 1;
//            Tour tour = new Tour(dateTour, nbrTr, membreTontine, demandeJointure.getTontine() );
//            tourService.saveTour(tour);
//        }
//        else {
//            Tour tour = new Tour();
//            tour.setMembreTontine(membreTontine);
//
//            ArrayList<Integer> listTours = new ArrayList<>();
//            ArrayList<Tour> Tlist = tourService.findTours();
//            for(Tour tour1:Tlist){
//                listTours.add(tour1.getNbrTour());
//            }
//            int nbrTour = generateUniqueRandomNumber(1,demandeJointure.getTontine().getMaxMembre(),listTours);
//            tour.setNbrTour(nbrTour);
//
//            int nbrJour;
//            if(demandeJointure.getTontine().getFrequence() == Demandetontine.Frequence.HEBDOMADAIRE)
//            {
//                nbrJour = 7;
//            } else if (demandeJointure.getTontine().getFrequence() == Demandetontine.Frequence.MENTUEL) {
//                nbrJour = 30;
//            }
//            else {
//                nbrJour = 90;
//            }
//
//            LocalDate dateTour = demandeJointure.getTontine().getDateDebut().plusDays((long) nbrJour * nbrTour) ;
//            tour.setDateTour(dateTour);
//            tour.setTontine(demandeJointure.getTontine());
//
//            tourService.saveTour(tour);
//
//        }
//
//    }
//
//
//
//    public static class RandomNumberGenerator {
//
//        public static int generateUniqueRandomNumber(int min, int max, ArrayList<Integer> existingNumbers) {
//            Random random = new Random();
//            int randomNumber;
//
//            do {
//                // Generate a random number within the specified range
//                randomNumber = random.nextInt(max - min + 1) + min;
//            } while (existingNumbers.contains(randomNumber));
//
//            return randomNumber;
//        }
//
//    }


    @GetMapping("/afficherMembres")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ModelAndView afficherMembres(@RequestParam(name = "tontineId") int tontineId, Model model){

        Tontine tontine = tontineService.findById(tontineId);

        if(tontine != null){
             List<MembreTontine> membreTontines =  (List<MembreTontine>) tontine.getMembreTontines();
             Collections.reverse(membreTontines);
             model.addAttribute("membresTontine", membreTontines);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/membresTontine");
        return modelAndView;
    }

    @GetMapping("supprimer-membre-{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView supprimerMembre(@PathVariable(name = "id") int id, RedirectAttributes redirectAttributes){

        try {
            membreServiceImp.deleteMembre(membreServiceImp.findMembreById(id));
            redirectAttributes.addFlashAttribute("successMessage", "Le membre est supprimé avec succès");
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une Erreur est Survenue Lors de la Suppression du membre");
        }

        return new ModelAndView("redirect:/tontines");
    }



    @GetMapping("/utilisateurs")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView utilisateurs(Model model){
        List<User> users = userRepository.findAll();
        if(!users.isEmpty()){
            Collections.reverse(users);
            model.addAttribute("users", users);
        }

        return new ModelAndView("admin/utilisateurs/index");
    }


    @GetMapping("supprimer-user-{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView supprimerUser(@PathVariable(name = "id") int id, RedirectAttributes redirectAttributes){
        try{
            User user = userRepository.findById(id).orElse(null);
            userRepository.delete(user);

            redirectAttributes.addFlashAttribute("successMessage", "L'utilisateur est supprimé avec succès");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une Erreur est Survenue Lors de la Suppression de l'Utilisateur");
        }
        return new ModelAndView("redirect:/utilisateurs");
    }

    @PostMapping("/modifier-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView modifierUser(@ModelAttribute(name = "user") User user, @RequestParam(name = "id") int user_id,
                                     RedirectAttributes redirectAttributes){
        try{
            User userF = userRepository.findById(user_id).orElse(null);

            if (userF != null) {
                userF.setEmail(user.getEmail());
                userF.setCin(user.getCin());
                userF.setNumTele(user.getNumTele());
                userF.setPassword(user.getPassword());
                userF.setNom_prenom(user.getNom_prenom());

                userRepository.save(userF);
            }

            redirectAttributes.addFlashAttribute("successMessage", "L'utilisateur est modifé avec succès");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une Erreur est Survenue Lors de la modification de l'Utilisateur");
        }

        return new ModelAndView("redirect:/utilisateurs");
    }

    @PostMapping("/ajouter-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView ajouterUser(@ModelAttribute(name = "user") User user, RedirectAttributes redirectAttributes){
        try{
            userRepository.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "L'utilisateur est ajouté avec succès");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une Erreur est Survenue Lors de l'ajout de l'Utilisateur");
        }

        return new ModelAndView("redirect:/utilisateurs");
    }

    @GetMapping("/groupes")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView groupes(Model model){
        List<GroupeUser> groupes = groupeUserRepository.findAll();
        if(!groupes.isEmpty()){
            Collections.reverse(groupes);
            model.addAttribute("groupes", groupes);
        }

        return new ModelAndView("admin/utilisateurs/groupes/groupes");
    }


    @GetMapping("/payer/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView payer(RedirectAttributes redirectAttributes, @PathVariable(name = "id") int id){
        try{
            MembreTontine membreTontine = membreServiceImp.findMembreById(id);
            if(membreTontine != null){
                membreTontine.setPaye(true);
                membreServiceImp.saveMembre(membreTontine);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Le Payment est passé pour ce Membre.");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une Erreur est Survenue lors du paiment.");
        }

        return new ModelAndView("redirect:/tontines");
    }

}


