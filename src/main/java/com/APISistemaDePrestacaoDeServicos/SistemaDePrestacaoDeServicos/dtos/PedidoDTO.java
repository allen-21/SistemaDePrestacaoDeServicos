package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.EstadoPedido;

import java.time.LocalDateTime;

public record PedidoDTO(Long servicoId, Long clienteId, String descricao, EstadoPedido status, LocalDateTime dataHora) {
}

