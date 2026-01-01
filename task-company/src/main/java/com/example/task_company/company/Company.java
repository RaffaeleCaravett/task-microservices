package com.example.task_company.company;

import com.example.task_company.codiceAccesso.CodiceAccesso;
import com.example.task_company.formaGiuridica.FormaGiuridica;
import com.example.task_company.indirizzo.Indirizzo;
import com.example.task_company.settore.Settore;
import com.example.task_company.subscription.Subscription;
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
public class Company implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String createdAt;
    private String deleteddAt;
    private Boolean isActive;
    private Boolean isConfirmed;
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
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne(mappedBy = "company",orphanRemoval = true,cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private CodiceAccesso codiceAccesso;
    @OneToOne(mappedBy = "company",orphanRemoval = true,cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Subscription subscription;
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
