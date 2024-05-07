package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.UserRole;

public record RegisterProfissionalDTO(String nome, String telefone, String endereco, String username, String password, String especialidades, Boolean disponibilidade, UserRole role) {
}
