package com.example.task_general.indirizzo.cap;

import com.example.task_general.indirizzo.regione.Regione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapRepository extends JpaRepository<Cap, Long>, JpaSpecificationExecutor<Cap> {
    List<Cap> findAllByCitta_Id(Long id);
}
