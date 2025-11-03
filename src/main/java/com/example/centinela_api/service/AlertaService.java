package com.example.centinela_api.service;

import com.example.centinela_api.modelos.Alerta;
import com.example.centinela_api.interfaces.IAlerta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertaService {

    @Autowired
    private IAlerta data;

    public List<Alerta> findAll() {
        return data.findAll();
    }

    public Optional<Alerta> findById(Integer id) {
        return data.findById(id);
    }

    public Alerta save(Alerta alerta) {
        return data.save(alerta);
    }

    public void deleteById(Integer id) {
        data.deleteById(id);
    }

    public Alerta update(Integer id, Alerta alertaDetails) {
        Optional<Alerta> existente = data.findById(id);
        if (existente.isPresent()) {
            Alerta alerta = existente.get();
            alerta.setRegion(alertaDetails.getRegion());
            alerta.setTitulo(alertaDetails.getTitulo());
            alerta.setDescripcion(alertaDetails.getDescripcion());
            alerta.setNivel(alertaDetails.getNivel());
            alerta.setUsuario(alertaDetails.getUsuario());
            return data.save(alerta);
        } else {
            return null;
        }
    }
}
