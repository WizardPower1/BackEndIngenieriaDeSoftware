package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudDeAmistadDTO { //DTO para tratar el JSON que se recibiría del POST.

    private Long idEmisor;

    private Long idReceptor;
}
