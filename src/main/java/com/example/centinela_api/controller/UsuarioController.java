package com.example.centinela_api.controller;



import com.example.centinela_api.modelos.Usuario;
import com.example.centinela_api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import com.example.centinela_api.modelos.LoginRequest;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/usuarios") // URL base: http://localhost:8080/api/usuarios
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // ==========================================================
    // 1. OBTENER TODOS LOS USUARIOS (READ ALL)
    // ==========================================================
    // GET http://localhost:8080/api/usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.findAll();
        // Devuelve 200 OK y la lista de usuarios
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    // ==========================================================
    // 2. OBTENER USUARIO POR ID (READ ONE)
    // ==========================================================
    // GET http://localhost:8080/api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) {
        // Usa map() para manejar el Optional devuelto por el servicio
        return usuarioService.findById(id)
                // Si existe: devuelve 200 OK
                .map(usuario -> new ResponseEntity<>(usuario, HttpStatus.OK))
                // Si NO existe: devuelve 404 NOT FOUND
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ==========================================================
    // 3. REGISTRAR NUEVO USUARIO (CREATE)
    // ==========================================================
    // POST http://localhost:8080/api/usuarios
    @PostMapping("/createUser")
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {

        if (usuarioService.findByCorreo(usuario.getCorreo()).isPresent()) {

            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Usuario nuevoUsuario = usuarioService.save(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // ==========================================================
    // 6. LOGIN (AUTENTICACIÓN)
    // ==========================================================
    // POST http://localhost:8080/api/usuarios/login
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest request) {
        Optional<Usuario> opt = usuarioService.authenticate(request.getCorreo(), request.getContrasena());
        if (opt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Usuario usuario = opt.get();
        // No devolver la contraseña
        usuario.setContrasena(null);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    // ==========================================================
    // 4. ACTUALIZAR USUARIO POR ID (UPDATE)
    // ==========================================================
    // PUT http://localhost:8080/api/usuarios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioDetails) {

        Usuario usuarioActualizado = usuarioService.update(id, usuarioDetails);

        if (usuarioActualizado != null) {
            // Devuelve 200 OK y el objeto actualizado
            return new ResponseEntity<>(usuarioActualizado, HttpStatus.OK);
        } else {
            // Devuelve 404 NOT FOUND si el ID no existe
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ==========================================================
    // 5. ELIMINAR USUARIO POR ID (DELETE)
    // ==========================================================
    // DELETE http://localhost:8080/api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {

        // Primero, verifica si el usuario existe antes de intentar eliminar
        if (usuarioService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404
        }

        usuarioService.deleteById(id);
        // Devuelve 204 NO CONTENT, indicando que la operación fue exitosa pero no hay cuerpo de respuesta
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}