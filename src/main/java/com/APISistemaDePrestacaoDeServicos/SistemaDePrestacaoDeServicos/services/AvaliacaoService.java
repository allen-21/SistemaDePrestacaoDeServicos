package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Avaliacao;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Cliente;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Pedido;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Servico;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.EstadoPedido;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.AvaliacaoRepository;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.PedidoRepository;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvaliacaoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private  AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private  ClienteService clienteService;
    @Autowired
    private ServicoRepository servicoRepository;

    public Avaliacao avaliarServico(Long pedidoId, int nota, String comentario) {
        Cliente clienteAutenticado = clienteService.getAuthenticatedCliente();
        if (clienteAutenticado == null) {
            throw new IllegalStateException("O cliente não está autenticado.");
        }

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        if (!pedido.getStatus().equals(EstadoPedido.FEITO)) {
            throw new IllegalStateException("O pedido não está no estado 'feito'.");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setPedido(pedido);
        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);
        return avaliacaoRepository.save(avaliacao);
    }

    public List<Avaliacao> listarAvaliacoesPorProfissional(Long profissionalId) {
        List<Servico> servicos = servicoRepository.findByProfissionalId(profissionalId);
        List<Pedido> pedidos = servicos.stream()
                .map(servico -> servico.getPedidos())
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return pedidos.stream()
                .map(pedido -> pedido.getAvaliacoes())
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

}
