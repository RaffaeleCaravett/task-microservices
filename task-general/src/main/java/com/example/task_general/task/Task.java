package com.example.task_general.task;

import com.example.task_general.project.Project;
import com.example.task_general.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate createdAt;
    private String title;
    private String description;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "task_utenti",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> developers;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id")
    private User creator;
    @Enumerated(EnumType.STRING)
    private TaskState taskState;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    private Project project;

}
