package com.example.centinela_api.controller;

import com.example.centinela_api.modelos.Reporte;
import com.example.centinela_api.modelos.ReporteDTO;
import com.example.centinela_api.modelos.CreateReporteRequest;
import com.example.centinela_api.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping
    public ResponseEntity<List<ReporteDTO>> getAllReportes() {
        var list = reporteService.findAll();

        // Ordenar por ID descendente (últimos primero)
        list.sort((r1, r2) -> r2.getReporteId().compareTo(r1.getReporteId()));
        // Map entities to DTOs to avoid JPA proxy/serialization issues and ensure unique objects
        List<ReporteDTO> dtos = list.stream().map(r -> {
            ReporteDTO d = new ReporteDTO();
            d.setReporteId(r.getReporteId());
            d.setDescripcion(r.getDescripcion());
            d.setLatitud(r.getLatitud());
            d.setLongitud(r.getLongitud());
            d.setTipo(r.getTipo() != null ? r.getTipo().name() : null);
            d.setEstado(r.getEstado() != null ? r.getEstado().name() : null);
            if (r.getUsuario() != null) {
                ReporteDTO.UsuarioMinDTO umin = new ReporteDTO.UsuarioMinDTO();
                umin.setUsuarioId(r.getUsuario().getUsuarioId());
                umin.setNombre(r.getUsuario().getNombre());
                d.setUsuario(umin);
            }
            if (r.getFoto() != null) {
                d.setFotoId(r.getFoto().getFotoId());
                d.setFotoUrl(r.getFoto().getUrlFoto());
            }
            return d;
        }).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reporte> getReporteById(@PathVariable Integer id) {
        return reporteService.findById(id)
                .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createReporte(@RequestBody CreateReporteRequest req) {
        try {
            Reporte nuevo = reporteService.createFromDto(req);

            // Convertir el Reporte creado a DTO (igual que en el GET)
            ReporteDTO dto = new ReporteDTO();
            dto.setReporteId(nuevo.getReporteId());
            dto.setDescripcion(nuevo.getDescripcion());
            dto.setLatitud(nuevo.getLatitud());
            dto.setLongitud(nuevo.getLongitud());
            dto.setTipo(nuevo.getTipo() != null ? nuevo.getTipo().name() : null);
            dto.setEstado(nuevo.getEstado() != null ? nuevo.getEstado().name() : null);
            if (nuevo.getUsuario() != null) {
                ReporteDTO.UsuarioMinDTO umin = new ReporteDTO.UsuarioMinDTO();
                umin.setUsuarioId(nuevo.getUsuario().getUsuarioId());
                umin.setNombre(nuevo.getUsuario().getNombre());
                dto.setUsuario(umin);
            }
            if (nuevo.getFoto() != null) {
                dto.setFotoId(nuevo.getFoto().getFotoId());
                dto.setFotoUrl(nuevo.getFoto().getUrlFoto());
            }

            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Reporte> updateReporte(@PathVariable Integer id, @RequestBody Reporte reporteDetails) {
        Reporte actualizado = reporteService.update(id, reporteDetails);
        if (actualizado != null) {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReporte(@PathVariable Integer id) {
        if (reporteService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        reporteService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
