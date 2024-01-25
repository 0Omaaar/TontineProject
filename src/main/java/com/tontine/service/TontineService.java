package com.tontine.service;

import com.tontine.entities.Tontine;
import com.tontine.repository.TontineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TontineService {
    @Autowired
    private TontineRepository tontineRepository;

    public List<Tontine> findTontineWithSorting(String field){
        return tontineRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }





}
