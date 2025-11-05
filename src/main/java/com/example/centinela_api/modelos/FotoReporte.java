package com.example.centinela_api.modelos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
// Match the actual DB table name used by the foreign key constraint
@Table(name = "fotosreportes")
public class FotoReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "foto_id")
    private Integer fotoId;

    @Column(name = "url_foto")
    private String urlFoto;
}
