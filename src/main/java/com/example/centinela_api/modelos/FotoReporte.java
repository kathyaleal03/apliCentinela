package com.example.centinela_api.modelos;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
// Use lowercase table name matching the existing DB foreign key constraint
@Table(name = "fotosreportes")
public class FotoReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "foto_id")
    private Integer fotoId;

    @Column(name = "url_foto")
    private String urlFoto;
}
