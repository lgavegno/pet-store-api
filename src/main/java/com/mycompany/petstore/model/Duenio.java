package com.mycompany.petstore.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "duenios")
public class Duenio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(name = "celular", nullable = false)
    private String celular;
    
    // Constructor, getters, and setters are handled by Lombok @Data
}
