package com.example.centinela_api.service;

import com.example.centinela_api.modelos.Reporte;
import com.example.centinela_api.interfaces.IReporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReporteService {

    @Autowired
    private IReporte data;

    public List<Reporte> findAll() {
        return data.findAll();
    }

    public Optional<Reporte> findById(Integer id) {
        return data.findById(id);
    }

    public Reporte save(Reporte reporte) {
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
            reporte.setUsuario(reporteDetails.getUsuario());
            return data.save(reporte);
        } else {
            return null;
        }
    }
}
