package com.example.task_auth.indirizzo.nazione;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NazioneRepository extends JpaRepository<Nazione, Long>, JpaSpecificationExecutor<Nazione> {
}