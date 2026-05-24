package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.DenunciaDeContenidoDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
@Service
public class ServicioModeracion { //Encargado de gestionar las denuncias.

	@Autowired
	private ServicioNotificacion notificationService; //Hacemos uso del servicio de notificaciones al final de los tramites de denuncias.

	@Autowired
	private usuarios usuarioRepo;

	@Autowired
	private publicaciones publicacionRepo;

	@Autowired
	private denuncias denunciaRepo;
	
	@Autowired
	private ServicioPost postService;
	
	public Denuncia registrarDenuncia(
			DenunciaDeContenidoDTO dto) {
		//Necesitamos comprobar que la publicación es válida y no existe denuncia previa igual.
		Usuario usuario = usuarioRepo.findById(dto.getIdUsuarioDenunciante()).orElseThrow();

		Publicacion publicacion = publicacionRepo.findById(dto.getIdPost()).orElseThrow();
		
		if(publicacion==null) throw new RuntimeException("Publicación inexistente");
		boolean existe = denunciaRepo.existeByUsuarioIdAndPublicacionIdAndMotivo(dto.getIdUsuarioDenunciante(), dto.getIdPost(), dto.getMotivo() );
		//Producimos error si se intenta producir una segunda denuncia idéntica.
		if (existe) throw new RuntimeException("Denuncia duplicada");
		//Si es válida se crea una denuncia y se meten los datos obtenidos del DTO.
		Denuncia denuncia = new Denuncia();

		denuncia.setUsuarioDenunciante(usuario);
		denuncia.setPublicacion(publicacion);
		denuncia.setMotivo(dto.getMotivo());
		denuncia.setEstado(EstadoDenuncia.PENDIENTE); //Siempre al crearla empieza en pendiente.
		publicacion.setEstado(EstadoDePublicacion.EN_REVISION);
		publicacion.setOculta(true); //Se oculta temporalmente la publicación

		publicacionRepo.save(publicacion);
		notificationService.notificar(); //A moderadores.

		return denunciaRepo.save(denuncia);	//Guardamos la nueva denuncia.
	}
	public void resolverDenuncia(Denuncia denuncia) { //Una vez el moderador toma su decisión cogemos la denuncia y actualizamos su estado
		Long idPost = denuncia.getPublicacion().getId();
		boolean decision = revisarContenido(denuncia); //True es denuncia válida.
		if(decision) {
			denuncia.setEstado(EstadoDenuncia.REVISADA);
			postService.eliminarPublicacion(idPost); //Eliminamos publicación si la denuncia se aprueba por el moderador.
			}
		denuncia.setEstado(EstadoDenuncia.RECHAZADA);
		postService.restaurarPublicacion(idPost); //Restauramos por completo la publicación si se rechaza la denuncia.
		
		denunciaRepo.save(denuncia); //Independientemente del resultado se marca y guarda, de manera que evitemos una denuncia igual otra vez.
	}
	
	
	public boolean revisarContenido(Denuncia denuncia) {
	    // simulación de decisión del moderador, como es solo esto lo meto aquí en el servicio pero esto no es lo correcto, se podría haber hecho con DTO (falta de tiempo)
	    return true; // o false
	}
}