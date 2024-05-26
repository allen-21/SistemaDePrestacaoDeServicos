package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.EstadoPedido;

public record PedidoProfissionalDTO(
        Long id, Long idProfissional, String nomeProfissional, String telefoneProfissional, String descricao, EstadoPedido statu
) {
}
