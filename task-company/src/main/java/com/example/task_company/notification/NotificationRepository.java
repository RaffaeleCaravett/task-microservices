package com.example.task_company.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long>, JpaSpecificationExecutor<Notification> {
    List<Notification> findByReceiver_Id(Long id);
    List<Notification> findByReceiver_IdAndStato(Long id,NotificationState stato);
}
