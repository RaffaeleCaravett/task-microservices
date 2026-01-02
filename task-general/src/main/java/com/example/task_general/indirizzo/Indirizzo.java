package com.example.task_general.indirizzo;

import com.example.task_general.company.Company;
import com.example.task_general.indirizzo.cap.Cap;
import com.example.task_general.indirizzo.citta.Citta;
import com.example.task_general.indirizzo.nazione.Nazione;
import com.example.task_general.indirizzo.regione.Regione;
import com.example.task_general.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "indirizzi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Indirizzo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nazione_id")
    private Nazione nazione;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "regione_id")
    private Regione regione;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "citta")
    private Citta citta;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cap_id")
    private Cap cap;
    private String via;
}
