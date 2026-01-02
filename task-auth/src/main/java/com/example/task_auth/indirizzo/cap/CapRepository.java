package com.example.task_auth.indirizzo.cap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapRepository extends JpaRepository<Cap, Long>, JpaSpecificationExecutor<Cap> {
    List<Cap> findAllByCitta_Id(Long id);
}
