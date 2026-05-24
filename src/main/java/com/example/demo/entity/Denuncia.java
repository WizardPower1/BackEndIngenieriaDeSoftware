package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Denuncia { //Clase que representa la entidad de denuncia y sus atributos.

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Usuario usuarioDenunciante;

	@ManyToOne
	private Publicacion publicacion;

	private String motivo;

	@Enumerated(EnumType.STRING)
	private EstadoDenuncia estado;
}
