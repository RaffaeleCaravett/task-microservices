package com.example.task_company.notification;

import com.example.task_company.company.Company;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;

@Entity
@Table(name = "user_notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate createdAt;
    private LocalDate readAt;
    @Enumerated(EnumType.STRING)
    private NotificationState stato;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    private User receiver;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_receiver_id")
    private User senderUser;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_company_id")
    private User senderCompany;
}
