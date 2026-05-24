package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Amistad { //Entidad que representa una amistad en la red social, tiene id para identificarlo y los usuarios involucrados en la amistad.

	@Id
	@GeneratedValue
	private Long id;

	private Usuario usuario1;

	private Usuario usuario2;

}
