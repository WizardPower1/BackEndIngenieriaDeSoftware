package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Usuario;

public interface usuarios extends JpaRepository<Usuario, Long> { //Repositorio de usuarios en la red social.
	Optional<Usuario> findByEmail(String email); //Usado para verificar que el email dado esta asignado a una cuenta/usuario ya existente.
}