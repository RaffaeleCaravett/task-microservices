package com.example.task_auth.codiceAccesso;

import com.example.task_auth.company.Company;
import com.example.task_auth.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "codice_accessi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodiceAccesso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Instant creationTime;
    private Boolean isUsed;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
