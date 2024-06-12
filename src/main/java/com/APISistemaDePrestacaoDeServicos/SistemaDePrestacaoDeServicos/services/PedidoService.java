package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.PedidoDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.PedidoProfissionalDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.*;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.EstadoPedido;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.AvaliacaoRepository;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.PedidoRepository;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProfissionalService profissionalService;

    public Pedido fazerPedido(PedidoDTO pedidoDTO) {
        Cliente clienteAutenticado = clienteService.getAuthenticatedCliente();
        if (clienteAutenticado != null) {
            Servico servico = servicoRepository.findById(pedidoDTO.servicoId())
                    .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));

            LocalDateTime dataHora = pedidoDTO.dataHora().withSecond(0).withNano(0);  // Remove segundos e nanos
            if (dataHora == null || dataHora.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Data e hora inválidas.");
            }

            Pedido novoPedido = new Pedido(servico, clienteAutenticado, pedidoDTO.descricao(), pedidoDTO.status(), dataHora);
            return pedidoRepository.save(novoPedido);
        } else {
            throw new IllegalStateException("Nenhum cliente autenticado encontrado.");
        }
    }

    public Pedido atualizarStatusPedido(Long pedidoId, EstadoPedido novoStatus) {
        Profissional profissionalAutenticado = profissionalService.getAuthenticatedProfissional();
        if (profissionalAutenticado != null) {
            Pedido pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
            pedido.setStatus(novoStatus);
            return pedidoRepository.save(pedido);
        } else {
            throw new IllegalStateException("Nenhum profissional autenticado encontrado.");
        }
    }


    public List<Pedido> listarPedidosPorProfissional() {
        Profissional profissionalAutenticado = profissionalService.getAuthenticatedProfissional();
        if (profissionalAutenticado != null) {
            List<Pedido> pedidos = pedidoRepository.findByServico_Profissional(profissionalAutenticado);
            List<Pedido> pedidosNaoAvaliados = new ArrayList<>();
            for (Pedido pedido : pedidos) {
                // Verificar se o pedido foi avaliado
                if (!pedido.foiAvaliado()) {
                    pedidosNaoAvaliados.add(pedido);
                }
            }
            return pedidosNaoAvaliados;
        } else {
            throw new IllegalStateException("Nenhum profissional autenticado encontrado.");
        }
    }


    public List<PedidoProfissionalDTO> listarPedidosNaoAvaliadosDoClienteAutenticado() {
        Cliente clienteAutenticado = clienteService.getAuthenticatedCliente();
        if (clienteAutenticado != null) {
            List<Pedido> pedidos = pedidoRepository.findByCliente(clienteAutenticado);
            List<PedidoProfissionalDTO> pedidosDTO = new ArrayList<>();
            for (Pedido pedido : pedidos) {
                // Verificar se o pedido foi avaliado
                if (!pedido.foiAvaliado()) {
                    Profissional profissional = (Profissional) pedido.getServico().getProfissional();
                    PedidoProfissionalDTO pedidoDTO = new PedidoProfissionalDTO(
                            pedido.getId(),
                            profissional.getId(),
                            profissional.getNome(),
                            profissional.getTelefone(),
                            pedido.getDescricao(),
                            pedido.getStatus()
                    );
                    pedidosDTO.add(pedidoDTO);
                }
            }
            return pedidosDTO;
        } else {
            throw new IllegalStateException("Nenhum cliente autenticado encontrado.");
        }
    }



    public boolean clienteAutenticadoAvaliarPedido(Long pedidoId) {
        Cliente clienteAutenticado = clienteService.getAuthenticatedCliente();
        if (clienteAutenticado != null) {
            Pedido pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
            if (!pedido.getStatus().equals(EstadoPedido.FEITO)) {
                throw new IllegalStateException("O pedido não está no estado 'feito'.");
            }
            Optional<Avaliacao> avaliacaoExistente = avaliacaoRepository.findByPedidoAndCliente(pedido, clienteAutenticado);
            return avaliacaoExistente.isEmpty();
        } else {
            throw new IllegalStateException("Nenhum cliente autenticado encontrado.");
        }
    }


}
