package com.example.task_company.piano;

import com.example.task_company.subscription.Subscription;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "piani")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Piano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titolo;
    private Integer prezzo;
    private String description;
    @OneToMany(mappedBy = "piano", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Subscription> subscription;
}
