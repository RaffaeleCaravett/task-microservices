package com.example.task_auth.indirizzo.cap;

import com.example.task_auth.indirizzo.citta.Citta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
