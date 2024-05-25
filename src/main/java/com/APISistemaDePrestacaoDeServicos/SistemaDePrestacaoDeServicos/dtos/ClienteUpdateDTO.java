package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos;

public record ClienteUpdateDTO(
        String nome,
        String telefone,
        String endereco,
        String password
) {
}