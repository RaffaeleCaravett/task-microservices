package com.example.task_auth.dimensioni;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DimensioneRepository extends JpaRepository<Dimensione, Long>, JpaSpecificationExecutor<Dimensione> {
}
