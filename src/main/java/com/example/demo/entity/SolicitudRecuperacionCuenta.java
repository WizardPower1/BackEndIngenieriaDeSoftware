package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudRecuperacionCuenta { //Representa la entidad de solicitud de recuperación de cuenta.

    @Id
    @GeneratedValue
    private Long id;

    private String email;
}