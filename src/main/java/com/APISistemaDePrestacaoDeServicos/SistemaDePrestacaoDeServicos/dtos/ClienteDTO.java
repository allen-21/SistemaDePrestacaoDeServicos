package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos;

public record ClienteDTO(
        Long id,
        String nome,
        String telefone,
        String endereco,
        String username
) {
}
