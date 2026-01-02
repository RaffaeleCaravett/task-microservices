package com.example.task_company.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository<UserNotification,Long>, JpaSpecificationExecutor<UserNotification> {
    List<UserNotification> findByReceiver_Id(Long id);
    List<UserNotification> findByReceiver_IdAndStato(Long id,NotificationState stato);
}