package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.Profissoes;

public record ProfissionalUpdateDTO(
        String nome,
        String telefone,
        String endereco,
        Profissoes profissoes,
        String especialidades,
        boolean disponibilidade,
        String password,
        String status
) {
}

