package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudAmistad { //Representa una solicitud de amistad generada por un emisor hacia un receptor.

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Usuario emisor;

    @ManyToOne
    private Usuario receptor;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitudAmistad estado; //Como las solicitudes quedan guardadas independientemente de que se acepten o se rechazen marcamos su estatus.
}