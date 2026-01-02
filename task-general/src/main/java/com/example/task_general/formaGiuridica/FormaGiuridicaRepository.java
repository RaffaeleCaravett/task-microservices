package com.example.task_general.formaGiuridica;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FormaGiuridicaRepository extends JpaRepository<FormaGiuridica,Long>, JpaSpecificationExecutor<FormaGiuridica> {
}
