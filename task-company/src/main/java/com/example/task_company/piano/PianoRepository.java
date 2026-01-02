package com.example.task_company.piano;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PianoRepository extends JpaRepository<Piano,Long>, JpaSpecificationExecutor<Piano> {
}
