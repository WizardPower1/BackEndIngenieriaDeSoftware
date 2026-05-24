package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Amistad;

public interface amistades extends JpaRepository<Amistad, Long> { //Repositorio de amistades
boolean existsByEmisorIdAndReceptorId(Long idEmisor, Long idReceptor); //Usado para buscar si dado dos personas, tienen una relacion de amistad ya existente.
}