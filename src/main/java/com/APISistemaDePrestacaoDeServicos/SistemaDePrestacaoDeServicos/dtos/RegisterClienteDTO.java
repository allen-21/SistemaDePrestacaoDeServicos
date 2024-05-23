package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.UserRole;

public record RegisterClienteDTO(String nome, String telefone, String endereco, String username, String password,
                                 UserRole role) {
}
