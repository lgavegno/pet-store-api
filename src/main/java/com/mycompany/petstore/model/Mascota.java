package com.mycompany.petstore.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "mascotas")
public class Mascota {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_cliente")
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private String raza;
    
    @Column(nullable = false)
    private String color;
    
    @Column(nullable = false)
    private String alergico;
    
    @Column(name = "atencion_especial")
    private String atencionEspecial;
    
    @Column(columnDefinition = "TEXT")
    private String observaciones;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "duenio_id", referencedColumnName = "id")
    private Duenio duenio;
    
    // Constructor, getters, and setters are handled by Lombok @Data
}
