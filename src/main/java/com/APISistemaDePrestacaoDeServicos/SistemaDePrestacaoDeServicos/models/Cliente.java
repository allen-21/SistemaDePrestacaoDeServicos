package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor

public class Cliente extends UserModel {


    public Cliente(String nome, String telefone, String endereco, String username, String password, UserRole role) {
        super(nome, telefone, endereco, username, password, UserRole.CLIENTE);

    }



}
