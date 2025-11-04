package com.example.centinela_api.modelos;

import lombok.Data;

@Data
public class CreateReporteRequest {
    private String tipo;
    private Double latitud;
    private Double longitud;
    private String descripcion;
    // frontend may send fotoUrl at top level
    private String fotoUrl;
    // nested usuario object; we only need usuarioId
    private UsuarioRef usuario;
    private String estado;

    @Data
    public static class UsuarioRef {
        private Integer usuarioId;
    }
}
