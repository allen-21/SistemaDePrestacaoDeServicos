package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.PedidoDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.PedidoProfissionalDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Cliente;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Pedido;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Servico;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.EstadoPedido;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.PedidoRepository;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProfissionalService profissionalService;

    public Pedido fazerPedido(PedidoDTO pedidoDTO) {
        Cliente clienteAutenticado = clienteService.getAuthenticatedCliente();
        if (clienteAutenticado != null) {
            Servico servico = servicoRepository.findById(pedidoDTO.servicoId())
                    .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));
            Pedido novoPedido = new Pedido(servico, clienteAutenticado, pedidoDTO.descricao(), pedidoDTO.status());
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
            return pedidoRepository.findByServico_Profissional(profissionalAutenticado);
        } else {
            throw new IllegalStateException("Nenhum profissional autenticado encontrado.");
        }
    }

    public List<PedidoProfissionalDTO> listarPedidosDoClienteAutenticado() {
        Cliente clienteAutenticado = clienteService.getAuthenticatedCliente();
        if (clienteAutenticado != null) {
            List<Pedido> pedidos = pedidoRepository.findByCliente(clienteAutenticado);
            List<PedidoProfissionalDTO> pedidosDTO = new ArrayList<>();
            for (Pedido pedido : pedidos) {
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
            return pedido.getStatus().equals(EstadoPedido.FEITO);
        } else {
            return false;
        }
    }
}
