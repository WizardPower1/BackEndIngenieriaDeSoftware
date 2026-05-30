package com.example.demo.controller;

// import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

// import com.example.demo.dto.FiltrarContenidoDTO;
import com.example.demo.dto.ResponderSolicitudAmistadDTO;
import com.example.demo.dto.SolicitudDeAmistadDTO;
// import com.example.demo.entity.Publicacion;
import com.example.demo.entity.SolicitudAmistad;
// import com.example.demo.service.ServicioFiltrado;
import com.example.demo.service.ServicioSocial;
@RestController
@RequestMapping("/solicitudAmistad")
public class SolicitudAmistadController {
	@Autowired
	private ServicioSocial socialService; //Usamos el servicio asociado a este controlador en el caso de uso.
	@PostMapping("/solicitar")
	public SolicitudAmistad mandarSolicitudDeAmistad(@RequestBody SolicitudDeAmistadDTO dto) {
		return socialService.gestionarSolicitudDeAmistad(dto); //Primero tratamos las excepciones y el envió y notificación de solicitud
	}
	//Si el receptor de la solicitud responde se trata la segunda parte.
	@PostMapping("/responder")
	public void responder(@RequestBody ResponderSolicitudAmistadDTO dto) {
		socialService.responderSolicitud(dto); //Aquí se toman las decisiones basado en si el receptor acepta o rechaza la solicitud.
	}
}
