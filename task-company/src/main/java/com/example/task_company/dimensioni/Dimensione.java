package com.example.task_company.dimensioni;

import com.example.task_company.company.Company;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "dimensioni")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dimensione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label;
    @Enumerated(EnumType.STRING)
    private Size dimensione;
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Company> companies;
}
