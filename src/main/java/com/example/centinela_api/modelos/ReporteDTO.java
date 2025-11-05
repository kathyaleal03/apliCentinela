package com.example.centinela_api.modelos;

import lombok.Data;

@Data
public class ReporteDTO {
    private Integer reporteId;
    private String descripcion;
    private Double latitud;
    private Double longitud;
    private String tipo;
    private String estado;
    private UsuarioMinDTO usuario; // nested minimal usuario
    private Integer fotoId;
    private String fotoUrl;

    @Data
    public static class UsuarioMinDTO {
        private Integer usuarioId;
        private String nombre;
    }
}
