package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models;


import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserModel implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;
    private String nome;
    private String telefone;
    private String endereco;

    @Column(unique = true)
    private String username;
    private String password;
    private UserRole role;

    public UserModel(String nome, String telefone, String endereco, String username, String password, UserRole role) {
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
        this.username = username;
        this.password = password;
        this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        if (this.role == UserRole.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (this.role == UserRole.CLIENTE) {
            authorities.add(new SimpleGrantedAuthority("ROLE_CLIENTE"));
        }
        if (this.role == UserRole.PROFISSIONAL) {
            authorities.add(new SimpleGrantedAuthority("ROLE_PROF"));
        }

        return authorities;
    }



    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
