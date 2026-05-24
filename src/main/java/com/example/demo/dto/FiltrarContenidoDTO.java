package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltrarContenidoDTO { //DTO para tratar el JSON que se recibiría del POST.

    private String tipo;

    private String texto;

}