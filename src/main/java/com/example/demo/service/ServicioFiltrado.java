package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.FiltrarContenidoDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
@Service
public class ServicioFiltrado {

	@Autowired
	private publicaciones publicacionRepository;

	public List<Publicacion> filtrar(FiltrarContenidoDTO dto) {
		//Cogemos todas las publicaciones y filtramos por las cosas que se permiten en el caso de uso, tipo de contenido y texto.
		List<Publicacion> publicaciones = publicacionRepository.findAll();

		return publicaciones.stream().filter(p -> filtrarPorTipo(p, dto.getTipo())).filter(p -> filtrarPorTexto(p, dto.getTexto())).toList();
	}
	//Filtra por tipo
	private boolean filtrarPorTipo(Publicacion p, String tipo) {
		if (tipo == null || tipo.isEmpty()) return true;

		return p.getTipo().name().equalsIgnoreCase(tipo);
	}
	//Filtra por el texto, para ello consultamos el contenido de la publicacion.
	private boolean filtrarPorTexto(Publicacion p, String texto) {
		if (texto == null || texto.isEmpty()) return true;

		return p.getContenido().toLowerCase().contains(texto.toLowerCase());
	}
}