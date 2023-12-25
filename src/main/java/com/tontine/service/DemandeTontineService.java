package com.tontine.service;

import com.tontine.entities.DemandeTontineEntite;
import com.tontine.entities.Demandetontine;
import com.tontine.repository.DemanderTontineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class DemandeTontineService {


    @Autowired
    DemanderTontineRepository demanderTontineRepository;


    public List<DemandeTontineEntite> findAll(){
        return demanderTontineRepository.findAll();
    }


}
