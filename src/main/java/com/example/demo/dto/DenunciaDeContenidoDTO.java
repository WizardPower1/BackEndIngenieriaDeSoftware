package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DenunciaDeContenidoDTO { //DTO para tratar el JSON que se recibiría del POST.

	private Long idPost;

	private String motivo;

	private Long idUsuarioDenunciante;
}
