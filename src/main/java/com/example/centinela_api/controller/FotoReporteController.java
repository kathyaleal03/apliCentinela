package com.example.centinela_api.controller;

import com.example.centinela_api.modelos.FotoReporte;
import com.example.centinela_api.service.FotoReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/fotos", "/api/fotosreportes"})
public class FotoReporteController {

    @Autowired
    private FotoReporteService fotoService;

    @GetMapping
    public ResponseEntity<List<FotoReporte>> getAll() {
        return new ResponseEntity<>(fotoService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FotoReporte> getById(@PathVariable Integer id) {
        return fotoService.findById(id)
                .map(f -> new ResponseEntity<>(f, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<FotoReporte> create(@RequestBody FotoReporte foto) {
        FotoReporte nuevo = fotoService.save(foto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FotoReporte> update(@PathVariable Integer id, @RequestBody FotoReporte fotoDetails) {
        FotoReporte actualizado = fotoService.update(id, fotoDetails);
        if (actualizado != null) return new ResponseEntity<>(actualizado, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (fotoService.findById(id).isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        fotoService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
