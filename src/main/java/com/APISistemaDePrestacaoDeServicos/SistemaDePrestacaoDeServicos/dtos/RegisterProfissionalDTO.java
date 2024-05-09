package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.Profissoes;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.UserRole;

public record RegisterProfissionalDTO(String nome, String telefone, String endereco, String username, String password, Profissoes profissoes, String especialidades, Boolean disponibilidade, UserRole role) {
}
