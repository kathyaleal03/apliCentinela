package com.example.centinela_api.service;

import com.example.centinela_api.modelos.Reporte;
import com.example.centinela_api.interfaces.IReporte;
import com.example.centinela_api.modelos.Usuario;
import com.example.centinela_api.modelos.FotoReporte;
import com.example.centinela_api.service.UsuarioService;
import com.example.centinela_api.service.FotoReporteService;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    private static final Logger logger = LoggerFactory.getLogger(ReporteService.class);

    @Autowired
    private IReporte data;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FotoReporteService fotoReporteService;

    @Transactional(readOnly = true)
    public List<Reporte> findAll() {
        // Load within a transaction to initialize lazy relations before serialization
        List<Reporte> list = data.findAll();
        // touch lazy properties to avoid LazyInitializationException when Jackson serializes later
        for (Reporte r : list) {
            if (r.getUsuario() != null) {
                // access id and a simple property to force initialization
                r.getUsuario().getUsuarioId();
                r.getUsuario().getNombre();
            }
            if (r.getFoto() != null) {
                r.getFoto().getFotoId();
                r.getFoto().getUrlFoto();
            }
        }
        return list;
    }

    public Optional<Reporte> findById(Integer id) {
        return data.findById(id);
    }

    @Transactional
    public Reporte save(Reporte reporte) {
        // Ensure usuario is a managed entity (avoid transient reference)
        if (reporte.getUsuario() == null || reporte.getUsuario().getUsuarioId() == null) {
            throw new IllegalArgumentException("El reporte debe incluir usuario con usuarioId");
        }

        Integer uid = reporte.getUsuario().getUsuarioId();
        Usuario u = usuarioService.findById(uid).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + uid));
        reporte.setUsuario(u);

        // Handle foto: if frontend sent a foto object with only url (no fotoId), save it first
        if (reporte.getFoto() != null) {
            FotoReporte foto = reporte.getFoto();
            if (foto.getFotoId() == null && foto.getUrlFoto() != null && !foto.getUrlFoto().isBlank()) {
                FotoReporte saved = fotoReporteService.save(foto);
                // assign the managed saved entity so JPA will persist the FK
                reporte.setFoto(saved);
            } else if (foto.getFotoId() != null) {
                // ensure managed instance
                FotoReporte existing = fotoReporteService.findById(foto.getFotoId()).orElse(null);
                reporte.setFoto(existing);
            }
        }

        return data.save(reporte);
    }

    /**
     * Create a Reporte from a CreateReporteRequest DTO.
     */
    @Transactional
    public Reporte createFromDto(com.example.centinela_api.modelos.CreateReporteRequest req) {
        if (req == null) throw new IllegalArgumentException("Request body vacío");

        Reporte reporte = new Reporte();
        reporte.setDescripcion(req.getDescripcion());
        reporte.setLatitud(req.getLatitud());
        reporte.setLongitud(req.getLongitud());
        reporte.setEstado(Reporte.EstadoReporte.Activo);
        if (req.getEstado() != null) {
            try {
                reporte.setEstado(Reporte.EstadoReporte.valueOf(req.getEstado()));
            } catch (IllegalArgumentException e) {
                // ignore and keep default
            }
        }

        // tipo
        if (req.getTipo() != null) {
            try {
                reporte.setTipo(Reporte.TipoReporte.valueOf(req.getTipo()));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Tipo de reporte inválido: " + req.getTipo());
            }
        }

        // usuario
        if (req.getUsuario() == null || req.getUsuario().getUsuarioId() == null) {
            throw new IllegalArgumentException("El reporte debe incluir usuario.usuarioId");
        }
        Integer uid = req.getUsuario().getUsuarioId();
        Usuario u = usuarioService.findById(uid).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + uid));
        reporte.setUsuario(u);

        // foto: if fotoUrl provided, save FotoReporte first
        if (req.getFotoUrl() != null && !req.getFotoUrl().isBlank()) {
            FotoReporte foto = new FotoReporte();
            foto.setUrlFoto(req.getFotoUrl());
            logger.info("Saving foto with url={}", req.getFotoUrl());
            FotoReporte saved = fotoReporteService.save(foto);
            if (saved == null || saved.getFotoId() == null) {
                logger.warn("FotoReporte save returned null or no id: {}", saved);
            } else {
                logger.info("FotoReporte saved id={}", saved.getFotoId());
            }
            reporte.setFoto(saved);
        }

        return data.save(reporte);
    }

    public void deleteById(Integer id) {
        data.deleteById(id);
    }

    public Reporte update(Integer id, Reporte reporteDetails) {
        Optional<Reporte> existente = data.findById(id);
        if (existente.isPresent()) {
            Reporte reporte = existente.get();
            reporte.setDescripcion(reporteDetails.getDescripcion());
            reporte.setLatitud(reporteDetails.getLatitud());
            reporte.setLongitud(reporteDetails.getLongitud());
            reporte.setFoto(reporteDetails.getFoto());
            reporte.setTipo(reporteDetails.getTipo());
            reporte.setEstado(reporteDetails.getEstado());
            // Resolve usuario to managed entity when updating
            if (reporteDetails.getUsuario() != null && reporteDetails.getUsuario().getUsuarioId() != null) {
                Integer uid = reporteDetails.getUsuario().getUsuarioId();
                Usuario u = usuarioService.findById(uid).orElse(null);
                reporte.setUsuario(u);
            }
            return data.save(reporte);
        } else {
            return null;
        }
    }
}
