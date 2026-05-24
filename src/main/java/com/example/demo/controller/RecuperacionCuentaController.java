package com.example.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.NuevaContrasenaDTO;
import com.example.demo.dto.RecuperarCuentaDTO;
import com.example.demo.service.ServicioAutenticacion;
@RestController
@RequestMapping("/recuperacion")
public class RecuperacionCuentaController {

	@Autowired
	private ServicioAutenticacion recuperacionService; //Usamos el servicio asociado al controlador en el caso de uso

	@PostMapping("/solicitar")
	public String solicitarRecuperacion(@RequestBody RecuperarCuentaDTO dto) {
		return recuperacionService.solicitarRecuperacion(dto.getEmail()); //Primero tratamos la parte de solicitud.
	}
	//Si todo va bien se llega a la parte en la que el usuario cambia sus datos.
	@PostMapping("/cambiar")
	public String cambiar(@RequestBody NuevaContrasenaDTO dto) { 
		return recuperacionService.cambiarContrasena(dto); //Eventualmente hay respuesta y cambiamos y guardamos sus datos de forma persistente.
	}
}