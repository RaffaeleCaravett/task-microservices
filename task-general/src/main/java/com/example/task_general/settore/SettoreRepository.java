package com.example.task_general.settore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SettoreRepository extends JpaRepository<Settore,Long>, JpaSpecificationExecutor<Settore> {
}
