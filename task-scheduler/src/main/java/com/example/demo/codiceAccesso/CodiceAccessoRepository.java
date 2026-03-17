package com.example.demo.codiceAccesso;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface CodiceAccessoRepository extends JpaRepository<CodiceAccesso,Long>, JpaSpecificationExecutor<CodiceAccesso> {

    static Specification<CodiceAccesso> isUsed(Boolean isUsed){
        if(isUsed == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isUsed"),isUsed);
    }

    static Specification<CodiceAccesso> creationTimeGreaterThan(Instant creationTime){
        if(creationTime == null) return null;

        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("creationTime"),creationTime);
    }

    @Modifying
    @Query("DELETE FROM CodiceAccesso c WHERE c.isUsed = false AND c.creationTime < :threshold")
    void deleteExpiredUnusedCodes(@Param("threshold") Instant threshold);
}
