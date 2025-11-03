package com.example.centinela_api.modelos;



import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Emergencias")
public class Emergencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emergencia_id")
    private Integer emergenciaId;

    // Relación ManyToOne con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private String mensaje;
    private Double latitud;
    private Double longitud;

    private Boolean atendido = false;
}