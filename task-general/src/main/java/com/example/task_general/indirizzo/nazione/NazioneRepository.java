package com.example.task_general.indirizzo.nazione;

import com.example.task_general.indirizzo.regione.Regione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NazioneRepository extends JpaRepository<Nazione, Long>, JpaSpecificationExecutor<Nazione> {
}