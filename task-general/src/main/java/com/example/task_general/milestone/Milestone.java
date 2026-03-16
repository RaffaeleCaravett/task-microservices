package com.example.task_general.milestone;

import com.example.task_general.company.Company;
import com.example.task_general.log.Log;
import com.example.task_general.user.User;
import com.example.task_general.user.UserService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mapstruct.control.MappingControl;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "milestone")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Company company;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDate creationDate;
    private Instant creationTime;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "log_id")
    private Log log;
}
