package com.example.centinela_api.modelos;
import lombok.Data;
import jakarta.persistence.*;
 

@Entity
@Data
@Table(name = "Alertas")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alerta_id")
    private Integer alertaId;

    // Relación ManyToOne con Region (puede ser NULL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    private String titulo;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private NivelAlerta nivel = NivelAlerta.Verde;

    // Relación con Usuario (id_usuario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // Enum para el campo 'nivel'
    public enum NivelAlerta {
        Verde, Amarillo, Naranja, Rojo
    }
}