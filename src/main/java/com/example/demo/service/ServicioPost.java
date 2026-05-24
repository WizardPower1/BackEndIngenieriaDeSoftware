package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Publicacion;
import com.example.demo.repository.publicaciones;
import com.example.demo.entity.EstadoDePublicacion;
@Service
public class ServicioPost { //Servicio de publicaciones, este se encargaría también de publicar pero como no forma parte del entorno de casos de uso
	                        //entonces solo tenemos eliminar publicacion (ocurre con una denuncia) o restaurar visibilidad (también ocurre tras una denuncia).
	@Autowired
    private ServicioNotificacion notificationService; //Usamos esto para notificar al autor de la publicación.
    @Autowired
    private publicaciones publicacionRepository; //obviamente necesitamos el repositorio de publicaciones para hacer cambios (save)

    public void eliminarPublicacion(Long idPost) {

        Publicacion p = publicacionRepository.findById(idPost).orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        p.setEstado(EstadoDePublicacion.ELIMINADA);

        publicacionRepository.save(p);
        notificationService.notificar();
    }

    public void restaurarPublicacion(Long idPost) {

        Publicacion p = publicacionRepository.findById(idPost).orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        p.setOculta(false);
        p.setEstado(EstadoDePublicacion.VISIBLE);

        publicacionRepository.save(p);
        notificationService.notificar();
    }
}