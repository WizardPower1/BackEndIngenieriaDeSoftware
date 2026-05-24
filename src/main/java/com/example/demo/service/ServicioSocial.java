package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.dto.ResponderSolicitudAmistadDTO;
import com.example.demo.dto.SolicitudDeAmistadDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
public class ServicioSocial { //Servicio encargado de la gestión de las solicitudes de amistad y amistades creadas.
	//Como siempre tenemos los repositorios necesarios:
	@Autowired
	private usuarios usuarioRepository;

	@Autowired
	private solicitudesAmistad solicitudRepository;

	@Autowired
	private amistades amistadRepository;

	@Autowired
	private ServicioNotificacion notificacionService; //Aquí también usamos el servicio de notificaciones para avisar al receptor de la solicitud
	                                                  //y también a los dos cuando se toma una decisión (rechazar o aceptar la solicitud)
	
	public SolicitudAmistad gestionarSolicitudDeAmistad(SolicitudDeAmistadDTO dto) {
		//Conseguimos al emisor y receptor, y además comprobamos que los dos sean válidos.
		Usuario emisor = usuarioRepository.findById(dto.getIdEmisor())
                .orElseThrow(() -> new RuntimeException("Emisor no existe"));
	        Usuario receptor = usuarioRepository.findById(dto.getIdReceptor())
	                .orElseThrow(() -> new RuntimeException("Receptor no existe"));

	        //Aquí comprobamos que no existe ya la relación de amistad.
	        boolean yaAmigos = amistadRepository
	                .existsByEmisorIdAndReceptorId(dto.getIdEmisor(), dto.getIdReceptor());

	        if (yaAmigos) {
	            throw new RuntimeException("Ya existe amistad");
	        }

	        //Aquí comprobamos que no existe una solicitud previa.
	        //De la manera que esta hecho esto ahora mismo implicaría que un
	        //usuario1 no puede enviar una solicitud a un usuario2 si usuario2 ya ha rechazado una solicitud de usuario1.
	        //Debatible si esto es algo bueno o malo para la aplicación.
	        boolean solicitudExiste = solicitudRepository
	                .existsByEmisorIdAndReceptorId(dto.getIdEmisor(), dto.getIdReceptor());

	        if (solicitudExiste) {
	            throw new RuntimeException("Solicitud ya existe");
	        }

	        //Si no hay solicitud previa se crea una con los datos pertinentes, quien ha solicitado y a quien.
	        SolicitudAmistad solicitud = new SolicitudAmistad();
	        solicitud.setEmisor(emisor);
	        solicitud.setReceptor(receptor);
	        solicitud.setEstado(EstadoSolicitudAmistad.PENDIENTE); //Siempre pendiente en cuanto se crea.

	        solicitudRepository.save(solicitud); //Guardamos en el repo para persistencia de datos.

	        //Notificación false a el receptor.
	        notificacionService.notificar();

	        return solicitud;
	    }
	public void responderSolicitud(ResponderSolicitudAmistadDTO dto) {
		//Obtenemos la respuesta y la solicitud asociada cuando responde el receptor
		//Por lo que si no existe (no debería pasar) se lanza excepcion.
	    SolicitudAmistad solicitud = solicitudRepository.findById(dto.getIdSolicitud())
	            .orElseThrow(() -> new RuntimeException("Solicitud no existe"));
	    //Luego en base a la respuesta dada como EstadoSolicitudDeAmistad se decide si crear una amistad o no.
	    if (dto.getRespuesta() == EstadoSolicitudAmistad.ACEPTADA) {

	        solicitud.setEstado(EstadoSolicitudAmistad.ACEPTADA);
	        //Se acepta solicitud, se crea amistad con usuario emisor y receptor, y se guarda y notifica.
	        Amistad amistad = new Amistad();
	        amistad.setUsuario1(solicitud.getEmisor());
	        amistad.setUsuario2(solicitud.getReceptor());

	        amistadRepository.save(amistad);

	        notificacionService.notificar();

	    } else if (dto.getRespuesta() == EstadoSolicitudAmistad.RECHAZADA) {
	    	//Se rechaza: entonces no se crea nada, se marca como rechazada y se notifica al emisor
	        solicitud.setEstado(EstadoSolicitudAmistad.RECHAZADA);

	        notificacionService.notificar();
	    }

	    solicitudRepository.save(solicitud); //La solicitud sea rechaza o aceptada se guarda le cambio en el repo.
	}
	}
