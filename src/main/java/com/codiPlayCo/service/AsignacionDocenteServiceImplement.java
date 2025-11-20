package com.codiPlayCo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codiPlayCo.model.AsignacionDocente;
import com.codiPlayCo.repository.AsignacionDocenteRepository;

@Service
public class AsignacionDocenteServiceImplement implements AsignacionDocenteService {

    @Autowired
    private AsignacionDocenteRepository asignacionDocenteRepository;

    @Override
    public List<AsignacionDocente> findAll() {
        return asignacionDocenteRepository.findAll();
    }

    @Override
    public AsignacionDocente findById(Integer id) {
        return asignacionDocenteRepository.findById(id).orElse(null);
    }

    @Override
    public void save(AsignacionDocente asignacionDocente) {
        asignacionDocenteRepository.save(asignacionDocente);
    }

    @Override
    public void deleteById(Integer id) {
        asignacionDocenteRepository.deleteById(id);
    }
}
