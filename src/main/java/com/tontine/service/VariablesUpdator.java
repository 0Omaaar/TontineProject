package com.tontine.service;

import com.tontine.entities.Demandetontine;
import com.tontine.entities.MembreTontine;
import com.tontine.entities.Tontine;
import com.tontine.entities.Tour;
import com.tontine.repository.TontineRepository;
import com.tontine.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class VariablesUpdator {

    @Autowired
    private TontineRepository tontineService;

    @Autowired
    private TourService tourService;

    @Autowired
    private MembreServiceImp membreServiceImp;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 28 0 * * *")
    public void updateVariable() {

        List<Tontine> tontineList = tontineService.findAll();
        List<Tour> tourList = tourService.findTours();

        for (Tontine tontine : tontineList) {

            long nbM = tontine.getMembreTontines().size();
            if (nbM != tontine.getMaxMembre()) {
                tontine.setStatutTontine(Tontine.StatutTontine.TERMINE);
                tontineService.save(tontine);
            } else{

                if (tontine.getDateDebut().equals(LocalDate.now())) {
//                     test tour_courant_suivant // plusDays(7)

                    tontine.setStatutTontine(Tontine.StatutTontine.EN_COURS);
                    tontineService.saveAndFlush(tontine);
                } else if (tontine.getDateFin().equals(LocalDate.now())) {
                    // test tour_courant_suivant // plusDays(7)

                    tontine.setStatutTontine(Tontine.StatutTontine.TERMINE);
                    tontineService.saveAndFlush(tontine);
                }
            }
        }


        for (Tour tour : tourList) {
            int nbrM = tour.getTontine().getMembreTontines().size();
            if (nbrM == tour.getTontine().getMaxMembre()) {
                List<MembreTontine> membresToModify = new ArrayList<>();

                int nbrJour;
                if (tour.getTontine().getFrequence() == Demandetontine.Frequence.HEBDOMADAIRE) {
                    nbrJour = 7;
                } else if (tour.getTontine().getFrequence() == Demandetontine.Frequence.MENTUEL) {
                    nbrJour = 30;
                } else {
                    nbrJour = 90;
                }

                LocalDate tourAffectation = tour.getDateTour().minusDays(nbrJour);
                // test tour_courant_suivant // plusDays(7)
                if (tourAffectation.equals(LocalDate.now())) {
                    Tontine tontine = tour.getTontine();
                    if(tontine.getTypeOrdre() == Demandetontine.TypeOrdre.ORDER){
                        tontine.setTourCourant((int) tour.getMembreTontine().getId());   //-1
                    }else{
                        tontine.setTourCourant((int) tour.getMembreTontine().getId());
                    }

                    //email traitment
                    String message = "<p>Bonjour,</p>"
                            + "<p>C'est votre tour pour tontine avec le Nom : <strong>" + tour.getTontine().getNom() + "</strong>.</p>"
                            + "Pour accéder à votre compte, veuillez cliquer sur le lien ci-dessous:<br/>"
                            + "<a href=\"http://localhost:9090/\">Accéder à la page d'accueil</a></p>";

                    emailService.sendSimpleMessage(tour.getMembreTontine().getUser().getEmail(), "Tour-"+tour.getTontine().getNom(), message);


                    tontineService.saveAndFlush(tontine);

                    for (MembreTontine membreTontine : tontine.getMembreTontines()) {
                        membresToModify.add(membreTontine);
                    }
                }

                for (MembreTontine membreTontine : membresToModify) {
                    membreTontine.setPaye(false);
                    membreServiceImp.saveMembre(membreTontine);
                }
            }
        }

    }


}