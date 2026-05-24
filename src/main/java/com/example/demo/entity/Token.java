package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token { //Representa un token para recuperar cuenta.

    @Id
    @GeneratedValue
    private Long id;

    private String token; //Parte importante que representa la clave del token.

    private Usuario usuarioDeEsteToken; //Usuario que recibio el token para usar.

    private boolean usado; //Una vez usado se marca como tal y guarda en el repositorio de tokens
}