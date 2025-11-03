package com.example.centinela_api.interfaces;

import com.example.centinela_api.modelos.FotoReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFotoReporte extends JpaRepository<FotoReporte, Integer> {

}
