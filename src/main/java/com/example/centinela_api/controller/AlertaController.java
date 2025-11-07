package com.example.centinela_api.controller;

import com.example.centinela_api.modelos.Alerta;
import com.example.centinela_api.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://centinela-frontend.vercel.app"}, allowCredentials = "true")
@RequestMapping("/api/alertas")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    @GetMapping("/getAllAlert")
    public ResponseEntity<List<Alerta>> getAllAlertas() {
        return new ResponseEntity<>(alertaService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alerta> getAlertaById(@PathVariable Integer id) {
        return alertaService.findById(id)
                .map(a -> new ResponseEntity<>(a, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/createAlert")
    public ResponseEntity<Alerta> createAlerta(@RequestBody Alerta alerta) {
        Alerta nuevo = alertaService.save(alerta);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alerta> updateAlerta(@PathVariable Integer id, @RequestBody Alerta alertaDetails) {
        Alerta actualizado = alertaService.update(id, alertaDetails);
        if (actualizado != null) {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlerta(@PathVariable Integer id) {
        if (alertaService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        alertaService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
