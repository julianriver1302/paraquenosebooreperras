package com.codiPlayCo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codiPlayCo.model.Rol;
import com.codiPlayCo.repository.IRolRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {

    @Autowired
    private IRolRepository rolRepository;

    public Optional<Rol> findByNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }
    
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }
    
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }
    
    public Optional<Rol> findDocenteRol() {
        return rolRepository.findAll().stream()
            .filter(rol -> "docente".equals(rol.getNombre()))
            .findFirst();
    }
    
   

}