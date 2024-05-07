package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models;


import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.UserRole;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor

public class Administrador extends UserModel {

    public Administrador(String username, String password, UserRole role) {
        super(null, null, null, username, password, UserRole.ADMIN);
    }

}
