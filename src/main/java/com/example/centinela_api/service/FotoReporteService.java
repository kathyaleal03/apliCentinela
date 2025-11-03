package com.example.centinela_api.service;

import com.example.centinela_api.modelos.FotoReporte;
import com.example.centinela_api.interfaces.IFotoReporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FotoReporteService {

    @Autowired
    private IFotoReporte data;

    public List<FotoReporte> findAll() { return data.findAll(); }

    public Optional<FotoReporte> findById(Integer id) { return data.findById(id); }

    public FotoReporte save(FotoReporte foto) { return data.save(foto); }

    public void deleteById(Integer id) { data.deleteById(id); }

    public FotoReporte update(Integer id, FotoReporte fotoDetails) {
        Optional<FotoReporte> existente = data.findById(id);
        if (existente.isPresent()) {
            FotoReporte f = existente.get();
            f.setUrlFoto(fotoDetails.getUrlFoto());
            return data.save(f);
        }
        return null;
    }
}
