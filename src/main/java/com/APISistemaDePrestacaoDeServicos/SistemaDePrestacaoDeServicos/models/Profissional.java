package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models;


import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.Profissoes;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Profissional extends UserModel {

    private Profissoes profissoes;
    private String especialidades;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean disponibilidade;

    @JsonIgnore
    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Servico> servicos;

    public Profissional(String nome, String telefone, String endereco, String username, String password,Profissoes profissoes,String especialidades, Boolean disponibilidade) {
        super(nome, telefone, endereco, username, password, UserRole.PROFISSIONAL);
        this.profissoes = profissoes;
        this.disponibilidade = disponibilidade;
        this.especialidades = especialidades;
    }

}
