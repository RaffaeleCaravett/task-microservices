package com.example.task_general.user;

import com.example.task_general.codiceAccesso.CodiceAccesso;
import com.example.task_general.company.Company;
import com.example.task_general.project.Project;
import com.example.task_general.task.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "utenti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends UserInfos implements UserDetails {

    private String nome;
    private String cognome;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private List<Company> companies;
    @OneToOne(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private CodiceAccesso codiceAccesso;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    @JsonIgnore
    private List<Task> tasksAsCreator;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "developers")
    @JsonIgnore
    private List<Task> tasks;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    @JsonIgnore
    private List<Project> projects;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "manager")
    @JsonIgnore
    private List<Project> projectsAsManager;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
