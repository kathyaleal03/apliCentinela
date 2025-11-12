package com.example.centinela_api.interfaces;

import com.example.centinela_api.modelos.Emergencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmergencia extends JpaRepository<Emergencia, Integer> {

	@Query("SELECT e.atendido, COUNT(e) FROM Emergencia e GROUP BY e.atendido")
	java.util.List<Object[]> countByAtendidoGroup();

}
