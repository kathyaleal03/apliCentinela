package com.example.centinela_api.controller;

import com.example.centinela_api.modelos.*;
import com.example.centinela_api.service.EstadisticasService;
import com.example.centinela_api.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estadisticas")
@CrossOrigin(origins = {"http://localhost:5173", "https://centinela-frontend.vercel.app"}, allowCredentials = "true")
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    @Autowired
    private ReporteService reporteService;

    // Reportes - reuse existing ReporteService methods
    @GetMapping("/reportes/tipos")
    public ResponseEntity<List<EstadisticaTipoDTO>> reportesByTipo() {
        return new ResponseEntity<>(reporteService.getCountsByTipo(), HttpStatus.OK);
    }

    @GetMapping("/reportes/estados")
    public ResponseEntity<List<EstadisticaEstadoDTO>> reportesByEstado() {
        return new ResponseEntity<>(reporteService.getCountsByEstado(), HttpStatus.OK);
    }

    @GetMapping("/reportes/regiones")
    public ResponseEntity<List<RegionCountDTO>> reportesByRegion() {
        return new ResponseEntity<>(reporteService.getCountsByRegion(), HttpStatus.OK);
    }

    @GetMapping("/reportes/heatmap")
    public ResponseEntity<List<HeatmapPointDTO>> reportesHeatmap(@RequestParam(name = "precision", required = false, defaultValue = "3") int precision) {
        return new ResponseEntity<>(reporteService.getHeatmapPoints(precision), HttpStatus.OK);
    }

    // Alertas
    @GetMapping("/alertas/niveles")
    public ResponseEntity<List<EstadisticaNivelAlertaDTO>> alertasByNivel() {
        return new ResponseEntity<>(estadisticasService.getAlertCountsByNivel(), HttpStatus.OK);
    }

    @GetMapping("/alertas/regiones")
    public ResponseEntity<List<RegionCountDTO>> alertasByRegion() {
        return new ResponseEntity<>(estadisticasService.getAlertCountsByRegion(), HttpStatus.OK);
    }

    // Emergencias
    @GetMapping("/emergencias/atendidos")
    public ResponseEntity<List<EstadisticaAtendidoDTO>> emergenciasByAtendido() {
        return new ResponseEntity<>(estadisticasService.getEmergenciasCountsByAtendido(), HttpStatus.OK);
    }

    @GetMapping("/emergencias/heatmap")
    public ResponseEntity<List<HeatmapPointDTO>> emergenciasHeatmap(@RequestParam(name = "precision", required = false, defaultValue = "3") int precision) {
        return new ResponseEntity<>(estadisticasService.getEmergenciasHeatmap(precision), HttpStatus.OK);
    }
}
