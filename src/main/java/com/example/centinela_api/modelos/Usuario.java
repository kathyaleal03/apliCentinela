package com.example.centinela_api.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "Usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer usuarioId;

    private String nombre;

    @Column(unique = true)
    private String correo;

    private String contrasena; // ¡Recuerda ENCRIPTAR en la práctica!

    private String telefono;
    private String departamento;
    private String ciudad;

    @Convert(converter = RegionAttributeConverter.class)
    private Region region;

    // Nota: la fecha de registro ya no forma parte del modelo según la nueva estructura

    // Enum que corresponde a la columna ENUM('Santa Ana Norte', ...)
    public enum Region {
        Santa_Ana_Norte, Santa_Ana_Sur, Santa_Ana_Este, Santa_Ana_Oeste
    }
}