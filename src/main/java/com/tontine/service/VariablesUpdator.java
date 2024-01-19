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
    private MembreService membreService;

    @Scheduled(cron = "0 0 0 * * *")
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
                    tontine.setStatutTontine(Tontine.StatutTontine.EN_COURS);
                    tontineService.saveAndFlush(tontine);
                } else if (tontine.getDateFin().equals(LocalDate.now())) {
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
                if (tourAffectation.equals(LocalDate.now())) {
                    Tontine tontine = tour.getTontine();
                    tontine.setTourCourant(Math.toIntExact(tour.getMembreTontine().getId()));
                    tontineService.saveAndFlush(tontine);

                    for (MembreTontine membreTontine : tontine.getMembreTontines()) {
                        membresToModify.add(membreTontine);
                    }
                }

                for (MembreTontine membreTontine : membresToModify) {
                    membreTontine.setPaye(false);
                    membreService.save(membreTontine);
                }
            }
        }

    }


}