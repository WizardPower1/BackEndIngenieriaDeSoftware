package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.SolicitudAmistad;

public interface solicitudesAmistad
        extends JpaRepository<SolicitudAmistad, Long> {

	boolean existsByEmisorIdAndReceptorId(Long idEmisor, Long idReceptor);

}