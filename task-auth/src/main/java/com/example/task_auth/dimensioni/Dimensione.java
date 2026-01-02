package com.example.task_general.dimensioni;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
