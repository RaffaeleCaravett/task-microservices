package com.example.task_company.indirizzo.cap;

import com.example.task_company.indirizzo.Indirizzo;
import com.example.task_company.indirizzo.citta.Citta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "cap")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "citta_id")
    @JsonIgnore
    private Citta citta;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "cap")
    @JsonIgnore
    private List<Indirizzo> indirizzos;
}
