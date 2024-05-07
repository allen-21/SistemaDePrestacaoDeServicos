package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos;

public record ProfissionalDTO( Long id,
                               String nome,
                               String telefone,
                               String endereco,
                               String username,
                               String especialidades,
                               boolean disponibilidade) {
}
