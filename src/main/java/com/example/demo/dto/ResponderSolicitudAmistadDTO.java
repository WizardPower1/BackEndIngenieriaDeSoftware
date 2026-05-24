package com.example.demo.dto;
import com.example.demo.entity.EstadoSolicitudAmistad;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponderSolicitudAmistadDTO { //DTO para tratar el JSON que se recibiría del POST.

    private Long idSolicitud;
    private EstadoSolicitudAmistad respuesta;
}