package com.example.task_general.indirizzo.regione;

import com.example.task_general.indirizzo.citta.Citta;
import com.example.task_general.indirizzo.nazione.Nazione;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "regioni")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Regione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nazione_id")
    @JsonIgnore
    private Nazione nazione;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Citta> citta;

}
