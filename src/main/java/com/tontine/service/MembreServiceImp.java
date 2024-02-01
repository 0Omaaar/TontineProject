package com.tontine.service;

import com.tontine.entities.*;
import com.tontine.repository.GroupeUserRepository;
import com.tontine.repository.MembreRepository;
import com.tontine.repository.User_GroupeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


import static com.tontine.service.MembreServiceImp.RandomNumberGenerator.generateUniqueRandomNumber;
@Service
public class MembreServiceImp {

    @Autowired
    private DemandeJointureService demandeJointureService;

    @Autowired
    private GroupeUserRepository groupeUserRepository;

    @Autowired
    private User_GroupeUserRepository userGroupeUserRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private MembreServiceImp membreServiceImp;

    @Autowired
    private TontineService tontineService;

    @Autowired
    private TourService tourService;

    @Autowired
    private MembreRepository membreRepository;

    public MembreTontine saveMembre(MembreTontine membreTontine) {
        return membreRepository.save(membreTontine);
    }

    public void saveMembreALlCases(int id, RedirectAttributes redirectAttributes){
        try{
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
                userService.saveUsr(demandeJointure.getUser());
                groupeUserRepository.save(groupeUser);
                userGroupeUserRepository.save(userGroupeUser);
                membreServiceImp.saveMembre(membreTontine);
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
                membreServiceImp.saveMembre(membreTontine);
                tontineService.save(tontine);

                //function to create the tour
                createTour(demandeJointure, membreTontine);

            }
            redirectAttributes.addFlashAttribute("successMessage", "La demande de jointure a été accepté avec succès");
        }
        catch(Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("dangerMessage", "Une Erreur Survenue lors de l'acceptation de la demande");
        }





    }

    public void createTour(DemandeJointure demandeJointure, MembreTontine membreTontine){
        if(demandeJointure.getTontine().getTypeOrdre().equals(Demandetontine.TypeOrdre.ORDER)){
//            if(demandeJointure.getTontine().getMembreTontines() == null){
//                System.out.println("aloo");
//                tour.setNbrTour(1);
//            }else{
//                tour.setNbrTour();
//            }
            int nbrJour;
            if(demandeJointure.getTontine().getFrequence().equals( Demandetontine.Frequence.HEBDOMADAIRE))
            {
                nbrJour = 7;
            } else if (demandeJointure.getTontine().getFrequence() == Demandetontine.Frequence.MENTUEL) {
                nbrJour = 30;
            }
            else {
                nbrJour = 90;
            }

            LocalDate dateTour = demandeJointure.getTontine().getDateDebut().plusDays((long) nbrJour *(demandeJointure.getTontine().getTourList().size()+1));
            int nbrTr = demandeJointure.getTontine().getTourList().size() + 1;
            Tour tour = new Tour(dateTour, nbrTr, membreTontine, demandeJointure.getTontine() );
            tourService.saveTour(tour);
        }
        else {
            Tour tour = new Tour();
            tour.setMembreTontine(membreTontine);

            ArrayList<Integer> listTours = new ArrayList<>();
            ArrayList<Tour> Tlist = tourService.findTours();
            for(Tour tour1:Tlist){
                listTours.add(tour1.getNbrTour());
            }
            int nbrTour = generateUniqueRandomNumber(1,demandeJointure.getTontine().getMaxMembre(),listTours);
            tour.setNbrTour(nbrTour);

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

            LocalDate dateTour = demandeJointure.getTontine().getDateDebut().plusDays((long) nbrJour * nbrTour) ;
            tour.setDateTour(dateTour);
            tour.setTontine(demandeJointure.getTontine());

            tourService.saveTour(tour);

        }

    }



    public static class RandomNumberGenerator {

        public static int generateUniqueRandomNumber(int min, int max, ArrayList<Integer> existingNumbers) {
            Random random = new Random();
            int randomNumber;

            do {
                // Generate a random number within the specified range
                randomNumber = random.nextInt(max - min + 1) + min;
            } while (existingNumbers.contains(randomNumber));

            return randomNumber;
        }

    }

    public MembreTontine findMembreById(int id){
        return membreRepository.findById(id).orElse(null);
    }

    public void deleteMembre(MembreTontine membreTontine){
        membreRepository.delete(membreTontine);
    }

}
