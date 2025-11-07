package com.example.centinela_api.controller;

import com.example.centinela_api.modelos.Comentario;
import com.example.centinela_api.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime; // Necesario para la fecha
import java.util.List;
import java.util.Optional; // Necesario para el update

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://centinela-frontend.vercel.app"}, allowCredentials = "true")
@RequestMapping("/api/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping
    public ResponseEntity<List<Comentario>> getAllComentarios() {
        return new ResponseEntity<>(comentarioService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comentario> getComentarioById(@PathVariable Integer id) {
        return comentarioService.findById(id)
                .map(c -> new ResponseEntity<>(c, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 🚀 MODIFICADO PARA LA FECHA DE CREACIÓN:
     * Respeta la fecha si ya viene (del frontend) o asigna la fecha actual.
     */
    @PostMapping
    public ResponseEntity<Comentario> createComentario(@RequestBody Comentario comentario) {
        // 1. Asigna la fecha actual del servidor si el objeto no la trae (aunque el frontend ya la debe enviar).
        // Esto sirve como un respaldo del lado del servidor.
        if (comentario.getFecha() == null) {
            comentario.setFecha(LocalDateTime.now());
        }

        Comentario nuevo = comentarioService.save(comentario);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    /**
     * 🚀 MODIFICADO PARA LA FECHA DE ACTUALIZACIÓN:
     * Obtiene el comentario existente y se asegura de que la fecha original no se pierda,
     * a menos que el comentarioDetails traiga una nueva fecha del frontend (para edición).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Comentario> updateComentario(@PathVariable Integer id, @RequestBody Comentario comentarioDetails) {

        Optional<Comentario> existingCommentOpt = comentarioService.findById(id);

        if (existingCommentOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Comentario comentarioExistente = existingCommentOpt.get();

        // 1. Preservar la fecha original si el DTO no la incluye (lo que ocurre si solo se envía el mensaje).
        // La fecha original se usa si comentarioDetails.getFecha() es nulo.
        if (comentarioDetails.getFecha() == null) {
            comentarioDetails.setFecha(comentarioExistente.getFecha());
        }

        // 2. Llama al servicio con el DTO que tiene la fecha original (o la fecha del PUT del frontend)
        Comentario actualizado = comentarioService.update(id, comentarioDetails);

        if (actualizado != null) {
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } else {
            // Este else ya no debería ejecutarse si el check anterior funciona, pero se mantiene la estructura.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComentario(@PathVariable Integer id) {
        if (comentarioService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        comentarioService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}