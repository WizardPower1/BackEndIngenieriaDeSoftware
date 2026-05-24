package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Publicacion;

public interface publicaciones extends JpaRepository<Publicacion, Long> { //Repositorio de publicaciones.
}
