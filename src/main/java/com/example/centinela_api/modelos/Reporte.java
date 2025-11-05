package com.example.centinela_api.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.FetchType;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "Reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reporte_id")
    private Integer reporteId;

    // Relación ManyToOne con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Convert(converter = TipoReporteAttributeConverter.class)
    private TipoReporte tipo;

    private String descripcion;


    private Double latitud;
    private Double longitud;

    // Relación con la entidad de fotos de reportes (foto_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foto_id")
    private FotoReporte foto;

    @Enumerated(EnumType.STRING)
    private EstadoReporte estado = EstadoReporte.Activo;

    // fecha_reporte ya no forma parte del modelo según la nueva estructura

    // Enum para el campo 'tipo'
    public enum TipoReporte {
        Calle_inundada, Paso_cerrado, Refugio_disponible, Otro
    }

    // Enum para el campo 'estado'
    public enum EstadoReporte {
        Activo, Atendido, Verificado
    }
}