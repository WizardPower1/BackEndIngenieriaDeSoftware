package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario { //Entidad usuario y sus atributos esenciales, dado a los casos de uso que hemos tenido que implementar
	                   //no se ha necesitado tener un atributo que represente las publicaciones de un usuario concreto.

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre; //Realmente no usado para nuestros casos de uso, pero esencial en una red social de verdad.

	private String email;

	private String contrasena;
}