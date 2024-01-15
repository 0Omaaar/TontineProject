package com.tontine.service;

import com.tontine.entities.Tour;
import com.tontine.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TourService{
    @Autowired
    private TourRepository tourRepository;

    public Tour saveTour(Tour tour){
        return tourRepository.save(tour);
    }

    public ArrayList<Tour> findTours() {
        return (ArrayList<Tour>) tourRepository.findAll();
    }

}
