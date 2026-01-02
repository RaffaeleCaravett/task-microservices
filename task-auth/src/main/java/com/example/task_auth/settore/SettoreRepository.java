package com.example.task_auth.settore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SettoreRepository extends JpaRepository<Settore,Long>, JpaSpecificationExecutor<Settore> {
}
