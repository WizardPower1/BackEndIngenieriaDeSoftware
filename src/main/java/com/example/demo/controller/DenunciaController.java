package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.DenunciaDeContenidoDTO;
import com.example.demo.entity.Denuncia;
import com.example.demo.service.ServicioModeracion;

@RestController
@RequestMapping("/denuncias")
public class DenunciaController {

	@Autowired
	private ServicioModeracion servicio; //Usamos el servicio asociado a este controlador en el caso de uso.

	@PostMapping("/denuncias")
	public Denuncia denunciar(
			@RequestBody DenunciaDeContenidoDTO dto) {

		return servicio.registrarDenuncia(dto); //Relegamos el trabajo al servicio.
	}
}