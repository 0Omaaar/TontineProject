package com.tontine.service;

import com.tontine.entities.Tontine;

import java.util.List;

public interface TontineService {
    public List<Tontine> findAll();

    public void deleteById(Integer id);
}
