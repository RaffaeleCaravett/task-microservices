package com.example.task_auth.company;


import com.example.task_auth.formaGiuridica.FormaGiuridica;
import com.example.task_auth.indirizzo.Indirizzo;
import com.example.task_auth.settore.Settore;
import com.example.task_auth.user.Role;
import com.example.task_auth.user.User;
import com.example.task_auth.user.UserInfos;
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
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company extends UserInfos implements UserDetails {

    private String ragioneSociale;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forma_giuridica_id")
    private FormaGiuridica formaGiuridica;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settore_id")
    private Settore settore;
    private String partitaIva;
    private String email;
    private String password;
    private String nomeAzienda;
    private String dimensioniAzienda;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Indirizzo> indirizzo;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Indirizzo> sedeLegale;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "companies_users",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;
    @Enumerated(EnumType.STRING)
    private Role role;

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
