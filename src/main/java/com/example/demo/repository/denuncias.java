package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Denuncia;

public interface denuncias extends JpaRepository<Denuncia, Long> { //Repositorio de denuncias.
	boolean existeByUsuarioIdAndPublicacionIdAndMotivo(Long usuarioId, Long publicacionId, String motivo); //Para buscar si hay una denuncia concreta existente ya.
}