package com.example.task_general.project;

import com.example.task_general.company.Company;
import com.example.task_general.projectType.ProjectType;
import com.example.task_general.task.Task;
import com.example.task_general.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate createdAt;
    private String title;
    private String description;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "progetti_utenti",
            joinColumns = @JoinColumn(name = "progetto_id"),
            inverseJoinColumns = @JoinColumn(name = "utente_id"))
    private List<User> users;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private User manager;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Task> tasks;
    @Enumerated(EnumType.STRING)
    private ProjectState projectState;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;
    @ManyToOne(fetch = FetchType.EAGER)
    private ProjectType projectType;
}
