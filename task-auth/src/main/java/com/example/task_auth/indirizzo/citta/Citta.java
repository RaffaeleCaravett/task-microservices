package com.example.task_auth.indirizzo.citta;

import com.example.task_auth.indirizzo.cap.Cap;
import com.example.task_auth.indirizzo.regione.Regione;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "citta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Citta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "regione_id")
    @JsonIgnore
    private Regione regione;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Cap> cap;
}
