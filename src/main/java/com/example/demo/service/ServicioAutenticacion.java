package com.example.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.NuevaContrasenaDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
@Service
public class ServicioAutenticacion { 
	//Servicio que tiene la responsabilidad de enviar emails de confirmacion, crear tokens y actualizar la información del usuario si así lo consigue.
	@Autowired
	private usuarios usuarioRepository;

	@Autowired
	private tokens tokenRepository;

	@Autowired
	private ServicioNotificacion notificacionService;
	public String solicitarRecuperacion(String email) {
		//Comprobamos que hay usuario asociado a ese email.
		Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

		if (usuario == null) {
			return "ERROR: usuario no existe";
		}
		//Se crea un token de confirmación
		Token token = new Token();
		token.setToken(UUID.randomUUID().toString());
		token.setUsuarioDeEsteToken(usuario);
		token.setUsado(false);
		//Guardamos en el repo de tokens para mantener consistencia y persistencia de datos.
		tokenRepository.save(token);
		//Avisamos al usuario con un email que contiene el token.
		notificacionService.enviarEmail(usuario.getEmail(),"Token: " + token.getToken());

		return "OK";
	}
	public String cambiarContrasena(NuevaContrasenaDTO dto) {

		Token token = tokenRepository.findByToken(dto.getToken()).orElse(null);
		//Si token invalido paramos.
		if (token == null || token.isUsado()) {
			return "TOKEN INVALIDO";
		}
		//Si el token es valido conseguimos al usuario que quiere cambiar la contraseña.
		Usuario usuario = token.getUsuarioDeEsteToken();
        //Cambiamos la contraseña y guardamos
		usuario.setContrasena(dto.getContrasena());
		usuarioRepository.save(usuario);
		//Marcamos como usado y guardamos.
		token.setUsado(true);
		tokenRepository.save(token);
		//Se notifica al usuario de los cambios.
		notificacionService.enviarEmail(
				usuario.getEmail(),
				"Contraseña cambiada correctamente"
				);

		return "OK";
	}
}