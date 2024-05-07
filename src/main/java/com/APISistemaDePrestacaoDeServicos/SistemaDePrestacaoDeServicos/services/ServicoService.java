package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services;


import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ServicoDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Servico;
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
        Profissional profissional = profissionalService.buscarPorId(idProfissional);
        if (profissional != null) {
            return servicoRepository.findByProfissional(profissional).stream()
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



    public List<Servico> listarTodosServicos() {
        return servicoRepository.findAll();
    }


}