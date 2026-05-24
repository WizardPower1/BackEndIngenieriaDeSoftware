package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Publicacion { //Representa una publicación de la red social, y una entidad del modelo de dominio.

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Como la identificación de una publicación no es nada serio la generamos en orden creciente.
	private Long id; 

	private TipoContenido tipo;

	private String contenido; //Encapsula descripción de publicación o la propia publicación si esta fuese solo texto.

	private boolean oculta; //Visibilidad al resto de gente, se oculta cuando se realiza una denuncia a la publicación.

	private EstadoDePublicacion Estado; 
	@ManyToOne
	private Usuario autor; //No realmente usado ya que no penalizamos a usuarios de publicaciones en nuestro caso de uso si la denuncia se aprueba.
}
