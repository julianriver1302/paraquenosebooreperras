package com.codiPlayCo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codiPlayCo.model.Mensaje;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {

    List<Mensaje> findByRemitenteIdOrderByFechaEnvioDesc(Integer remitenteId);

    List<Mensaje> findByDestinatarioIdOrderByFechaEnvioDesc(Integer destinatarioId);
}
