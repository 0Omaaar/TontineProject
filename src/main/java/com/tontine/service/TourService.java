package com.tontine.service;

import com.tontine.entities.Tour;
import com.tontine.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourService{
    @Autowired
    private TourRepository tourRepository;

    public Tour saveTour(Tour tour){
        return tourRepository.save(tour);
    }
}
