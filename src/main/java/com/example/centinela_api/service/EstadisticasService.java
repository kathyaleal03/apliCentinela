package com.example.centinela_api.service;

import com.example.centinela_api.interfaces.IAlerta;
import com.example.centinela_api.interfaces.IEmergencia;
import com.example.centinela_api.interfaces.IRegion;
import com.example.centinela_api.modelos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EstadisticasService {

    @Autowired
    private IAlerta iAlerta;

    @Autowired
    private IEmergencia iEmergencia;

    @Autowired
    private IRegion iRegion;

    @Autowired
    private ReporteService reporteService; // reuse report methods

    public List<EstadisticaNivelAlertaDTO> getAlertCountsByNivel() {
        List<Object[]> rows = iAlerta.countByNivelGroup();
        List<EstadisticaNivelAlertaDTO> out = new ArrayList<>();
        if (rows != null) {
            for (Object[] r : rows) {
                EstadisticaNivelAlertaDTO dto = new EstadisticaNivelAlertaDTO();
                dto.setNivel(r[0] != null ? r[0].toString() : "UNKNOWN");
                dto.setCantidad(r[1] instanceof Number ? ((Number) r[1]).longValue() : 0L);
                out.add(dto);
            }
        }
        return out;
    }

    public List<RegionCountDTO> getAlertCountsByRegion() {
        List<Region> regiones = iRegion.findAll();
        Map<Integer, Long> counts = new HashMap<>();
        List<Alerta> alertas = iAlerta.findAll();
        for (Alerta a : alertas) {
            if (a.getRegion() != null) {
                Integer rid = a.getRegion().getRegionId();
                counts.put(rid, counts.getOrDefault(rid, 0L) + 1L);
            }
        }
        List<RegionCountDTO> out = new ArrayList<>();
        for (Region r : regiones) {
            RegionCountDTO dto = new RegionCountDTO();
            dto.setRegionId(r.getRegionId());
            dto.setRegionNombre(r.getNombre());
            dto.setCantidad(counts.getOrDefault(r.getRegionId(), 0L));
            out.add(dto);
        }
        return out;
    }

    public List<EstadisticaAtendidoDTO> getEmergenciasCountsByAtendido() {
        List<Object[]> rows = iEmergencia.countByAtendidoGroup();
        List<EstadisticaAtendidoDTO> out = new ArrayList<>();
        if (rows != null) {
            for (Object[] r : rows) {
                EstadisticaAtendidoDTO dto = new EstadisticaAtendidoDTO();
                dto.setAtendido(r[0] instanceof Boolean ? (Boolean) r[0] : null);
                dto.setCantidad(r[1] instanceof Number ? ((Number) r[1]).longValue() : 0L);
                out.add(dto);
            }
        }
        return out;
    }

    public List<HeatmapPointDTO> getEmergenciasHeatmap(int precision) {
        List<Emergencia> list = iEmergencia.findAll();
        Map<String, Long> map = new HashMap<>();
        for (Emergencia e : list) {
            if (e.getLatitud() == null || e.getLongitud() == null) continue;
            double lat = round(e.getLatitud(), precision);
            double lon = round(e.getLongitud(), precision);
            String key = lat + ":" + lon;
            map.put(key, map.getOrDefault(key, 0L) + 1L);
        }
        List<HeatmapPointDTO> out = new ArrayList<>();
        for (Map.Entry<String, Long> en : map.entrySet()) {
            String[] p = en.getKey().split(":" );
            HeatmapPointDTO dto = new HeatmapPointDTO();
            dto.setLatitud(Double.parseDouble(p[0]));
            dto.setLongitud(Double.parseDouble(p[1]));
            dto.setCantidad(en.getValue());
            out.add(dto);
        }
        return out;
    }

    // reuse helpers
    private static double round(double value, int decimals) {
        if (decimals < 0) return value;
        double factor = Math.pow(10, decimals);
        return Math.round(value * factor) / factor;
    }
}
