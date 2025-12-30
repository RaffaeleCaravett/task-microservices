package com.example.task_general.indirizzo.citta;

import com.example.task_general.indirizzo.regione.Regione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CittaRepository extends JpaRepository<Citta, Long>, JpaSpecificationExecutor<Citta> {
    List<Citta> findAllByRegione_Id(Long id);
}