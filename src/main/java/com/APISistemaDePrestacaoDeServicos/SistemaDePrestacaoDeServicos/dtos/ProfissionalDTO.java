package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.Profissoes;

public record ProfissionalDTO(Long id,
                              String nome,
                              String telefone,
                              String endereco,
                              String username,
                              Profissoes profissoes,
                              boolean disponibilidade) {
}
