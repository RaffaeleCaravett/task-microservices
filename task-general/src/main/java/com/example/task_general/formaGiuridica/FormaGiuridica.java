package com.example.task_general.formaGiuridica;

import com.example.task_general.company.Company;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "forme_giuridiche")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormaGiuridica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Company> companies;
}