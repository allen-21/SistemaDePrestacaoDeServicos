package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.controller;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.PedidoDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Pedido;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.EstadoPedido;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/enviar")
    public ResponseEntity<Pedido> fazerPedido(@RequestBody PedidoDTO pedidoDTO) {
        try {
            Pedido pedido = pedidoService.fazerPedido(pedidoDTO);
            return ResponseEntity.ok(pedido);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @PutMapping("/status/{pedidoId}")
    public ResponseEntity<String> atualizarStatusPedido(@PathVariable Long pedidoId, @RequestBody Map<String, String> request) {
        try {
            String novoStatus = request.get("novoStatus");
            EstadoPedido status = EstadoPedido.valueOf(novoStatus);
            pedidoService.atualizarStatusPedido(pedidoId, status);
            return ResponseEntity.ok("Status do pedido atualizado com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erro: Status inválido.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).body("Erro: Não autorizado.");
        }
    }



    @GetMapping("/profissional")
    public ResponseEntity<List<Pedido>> listarPedidosPorProfissional() {
        try {
            List<Pedido> pedidos = pedidoService.listarPedidosPorProfissional();
            return ResponseEntity.ok(pedidos);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/cliente")
    public ResponseEntity<List<Pedido>> listarPedidosDoClienteAutenticado() {
        try {
            List<Pedido> pedidos = pedidoService.listarPedidosDoClienteAutenticado();
            return ResponseEntity.ok(pedidos);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/avaliacao/{pedidoId}")
    public ResponseEntity<Boolean> clienteAutenticadoAvaliarPedido(@PathVariable Long pedidoId) {
        try {
            boolean podeAvaliar = pedidoService.clienteAutenticadoAvaliarPedido(pedidoId);
            return ResponseEntity.ok(podeAvaliar);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
