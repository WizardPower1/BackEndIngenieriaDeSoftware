package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.SolicitudRecuperacionCuenta;

public interface solicitudesRecuperacionCuenta extends JpaRepository<SolicitudRecuperacionCuenta, Long> {
	//Repositorio de solicitudes para recuperar cuenta.
}