package com.example.task_auth.image;

import com.example.task_auth.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "immagini")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "IMMAGINE")
    private byte[] image;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UTENTE_ID")
    @JsonIgnore
    private User user;
}
