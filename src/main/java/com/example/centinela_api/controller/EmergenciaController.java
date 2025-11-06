package com.example.centinela_api.controller;

import com.example.centinela_api.modelos.Emergencia;
import com.example.centinela_api.service.EmergenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
@RequestMapping("/api/emergencias")
public class EmergenciaController {

    @Autowired
    private EmergenciaService emergenciaService;

    @GetMapping
    public ResponseEntity<List<Emergencia>> getAllEmergencias() {
        return new ResponseEntity<>(emergenciaService.findAll(), HttpStatus.OK);
    }

    //Comment
    @GetMapping("/{id}")
    public ResponseEntity<Emergencia> getEmergenciaById(@PathVariable Integer id) {
        return emergenciaService.findById(id)
                .map(e -> new ResponseEntity<>(e, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Emergencia> createEmergencia(@RequestBody Emergencia emergencia) {
        Emergencia nuevo = emergenciaService.save(emergencia);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Emergencia> updateEmergencia(@PathVariable Integer id, @RequestBody Emergencia emergenciaDetails) {
        Emergencia actualizado = emergenciaService.update(id, emergenciaDetails);
        if (actualizado != null) {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmergencia(@PathVariable Integer id) {
        if (emergenciaService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        emergenciaService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
