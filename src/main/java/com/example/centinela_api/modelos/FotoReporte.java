package com.example.centinela_api.modelos;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "FotosReportes")
public class FotoReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "foto_id")
    private Integer fotoId;

    @Column(name = "url_foto")
    private String urlFoto;
}
