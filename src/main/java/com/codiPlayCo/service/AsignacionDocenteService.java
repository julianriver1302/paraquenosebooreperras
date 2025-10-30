package com.codiPlayCo.service;

import java.util.List;
import com.codiPlayCo.model.AsignacionDocente;

public interface AsignacionDocenteService {
    List<AsignacionDocente> findAll();
    AsignacionDocente findById(Integer id);
    void save(AsignacionDocente asignacionDocente);
    void deleteById(Integer id);
}
