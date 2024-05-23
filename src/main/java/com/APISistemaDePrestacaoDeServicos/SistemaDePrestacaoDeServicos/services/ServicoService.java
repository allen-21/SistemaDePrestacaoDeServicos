package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services;


import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.AvaliacaoResponseDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ServicoDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Avaliacao;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Servico;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.AvaliacaoRepository;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ServicoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;


@Service
public class ServicoService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;




    public Servico adicionarServico(ServicoDTO servicoDTO) {
        Profissional profissional = profissionalService.getAuthenticatedProfissional();
        if (profissional != null) {
            Servico novoServico = new Servico(profissional, servicoDTO.descricaoDoServico());
            return servicoRepository.save(novoServico);
        } else {
            throw new IllegalStateException("Nenhum profissional autenticado encontrado.");
        }
    }

    public void excluirServico(Long servicoId) {
        Optional<Servico> optionalServico = servicoRepository.findById(servicoId);
        if (optionalServico.isPresent()) {
            Servico servico = optionalServico.get();
            Profissional profissional = profissionalService.getAuthenticatedProfissional();
            if (profissional != null && servico.getProfissional().equals(profissional)) {
                servicoRepository.delete(servico);
            } else {
                throw new IllegalStateException("O profissional autenticado não é o proprietário do serviço.");
            }
        } else {
            throw new NoSuchElementException("Serviço não encontrado com o ID: " + servicoId);
        }
    }
    public List<ServicoDTO> listarServicosDoProfissional(Long idProfissional) {
        var profissional = profissionalService.buscarPorId(idProfissional);
        if (profissional != null) {
            List<Servico> servicos = servicoRepository.findByProfissional(profissional);
            return servicos.stream()
                    .map(servico -> new ServicoDTO(servico.getId(), servico.getDescricaoDoServico()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<ServicoDTO> listarServicosDoProfissionalAutenticado() {
        Profissional profissionalAutenticado = profissionalService.getAuthenticatedProfissional();
        if (profissionalAutenticado != null) {
            List<Servico> servicos = servicoRepository.findByProfissional(profissionalAutenticado);
            return servicos.stream()
                    .map(servico -> new ServicoDTO(servico.getId(), servico.getDescricaoDoServico()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<AvaliacaoResponseDTO> listarAvaliacoesDoProfissional() {
        Profissional profissionalAutenticado = profissionalService.getAuthenticatedProfissional();
        if (profissionalAutenticado != null) {
            List<Servico> servicosDoProfissional = servicoRepository.findByProfissional(profissionalAutenticado);
            Set<Servico> servicosSet = new HashSet<>(servicosDoProfissional);
            List<Avaliacao> avaliacoesDoProfissional = avaliacaoRepository.findByPedido_ServicoIn(servicosSet);

            return avaliacoesDoProfissional.stream()
                    .map(avaliacao -> new AvaliacaoResponseDTO(
                            avaliacao.getId(),
                            avaliacao.getNota(),
                            avaliacao.getComentario(),
                            avaliacao.getPedido().getId()
                    ))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<AvaliacaoResponseDTO> listarAvaliacoesDoServico(Long servicoId) {
        Servico servico = servicoRepository.findById(servicoId)
                .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado com ID: " + servicoId));

        return servico.getPedidos().stream()
                .flatMap(pedido -> pedido.getAvaliacoes().stream())
                .map(avaliacao -> new AvaliacaoResponseDTO(
                        avaliacao.getId(),
                        avaliacao.getNota(),
                        avaliacao.getComentario(),
                        avaliacao.getPedido().getId()
                ))
                .collect(Collectors.toList());
    }




    public List<Servico> listarTodosServicos() {
        return servicoRepository.findAll();
    }




}