package com.example.centinela_api.modelos;

import lombok.Data;

@Data
public class HeatmapPointDTO {
    private Double latitud;
    private Double longitud;
    private Long cantidad;
}
