package com.example.centinela_api.interfaces;

import com.example.centinela_api.modelos.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IAlerta extends JpaRepository<Alerta, Integer> {

	@Query("SELECT a.nivel, COUNT(a) FROM Alerta a GROUP BY a.nivel")
	java.util.List<Object[]> countByNivelGroup();

}
