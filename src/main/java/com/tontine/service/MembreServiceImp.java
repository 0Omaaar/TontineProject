package com.tontine.service;

import com.tontine.entities.MembreTontine;
import com.tontine.repository.MembreRepository;
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
public class MembreServiceImp {
    @Autowired
    private MembreRepository membreRepository;

    public MembreTontine saveMembre(MembreTontine membreTontine) {
        return membreRepository.save(membreTontine);
    }

    public MembreTontine findMembreById(int id){
        return membreRepository.findById(id).orElse(null);
    }

    public void deleteMembre(MembreTontine membreTontine){
        membreRepository.delete(membreTontine);
    }

}
