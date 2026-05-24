package com.example.demo.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.FiltrarContenidoDTO;
import com.example.demo.entity.Publicacion;
import com.example.demo.service.ServicioFiltrado;
@RestController
@RequestMapping("/filtros")
public class FiltradoController {

	@Autowired
	private ServicioFiltrado filtradoService; //Usamos el servicio asociado a este controlador en el caso de uso.

	@PostMapping
	public List<Publicacion> filtrar(@RequestBody FiltrarContenidoDTO dto) {
		return filtradoService.filtrar(dto); //Relegamos el trabajo al servicio
	}
}
