package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class ServicioNotificacion { //Este servicio actúa como una caja negra de manera que podamos representar las notificaciones a usuarios sin implementar un servicio real.
	//Funciona ya que no tenemos usuarios reales e igualmente esto no se haría en java si no mediante un servicio externo.
public void notificar() {}
public void enviarEmail(String email, String mensaje) {
    System.out.println("EMAIL a " + email + ": " + mensaje);
}
}
