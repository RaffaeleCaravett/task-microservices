package com.example.task_general.secretCode;

import com.example.task_general.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name="secret_code")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecretCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate creationDate;
    private String code;
    private Boolean notified;
    private Boolean valid;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
