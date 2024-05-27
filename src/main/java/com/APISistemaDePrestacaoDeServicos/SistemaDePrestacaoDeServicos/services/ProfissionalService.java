package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ProfissionalDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ProfissionalUpdateDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.RegisterProfissionalDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ServicoDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Avaliacao;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Pedido;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Servico;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.Profissoes;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ProfissionalRepository;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfissionalService {

    @Autowired
    private ProfissionalRepository profissionalRepository;
    @Autowired
    private ServicoRepository servicoRepository;


    public Profissional salvarProfissional(RegisterProfissionalDTO profissionalDTO) {
        if (verificarExistenciaUsername(profissionalDTO.username())) {
            throw new IllegalArgumentException("Já existe um profissional com o nome de usuário: " + profissionalDTO.username());
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(profissionalDTO.password());

        Profissional profissional = new Profissional(profissionalDTO.nome(), profissionalDTO.telefone(),
                profissionalDTO.endereco(), profissionalDTO.username(), encryptedPassword,
                profissionalDTO.profissoes(), profissionalDTO.especialidades(), profissionalDTO.disponibilidade());
        return profissionalRepository.save(profissional);
    }
    public void atualizarProfissionalAutenticado(ProfissionalUpdateDTO profissionalDTO) {
        Profissional profissionalAutenticado = getAuthenticatedProfissional();
        if (profissionalAutenticado != null) {
            profissionalAutenticado.setNome(profissionalDTO.nome());
            profissionalAutenticado.setTelefone(profissionalDTO.telefone());
            profissionalAutenticado.setEndereco(profissionalDTO.endereco());
            profissionalAutenticado.setProfissoes(profissionalDTO.profissoes());
            profissionalAutenticado.setEspecialidades(profissionalDTO.especialidades());
            profissionalAutenticado.setDisponibilidade(profissionalDTO.disponibilidade());

            // Atualizar a senha se uma nova senha for fornecida
            if (profissionalDTO != null && !profissionalDTO.password().isEmpty()) {
                profissionalAutenticado.setPassword(new BCryptPasswordEncoder().encode(profissionalDTO.password()));
            }

            // Salvar as alterações
            profissionalRepository.save(profissionalAutenticado);

        } else {
            throw new IllegalStateException("Nenhum profissional autenticado encontrado.");
        }
    }

    public boolean verificarExistenciaUsername(String username) {
        return profissionalRepository.existsByUsername(username);
    }

   public Iterable<Profissional> listar(){
        return profissionalRepository.findAll();
   }
    public void excluirPorId(Long id) {
        profissionalRepository.deleteById(id);
    }

    public Profissional buscarPorUsername(String username) {
        return profissionalRepository.findByUsername(username);
    }
    public Profissional buscarPorId(Long id){
        Optional<Profissional> profissional = profissionalRepository.findById(id);
        return profissional.orElse(null);
    }
    public ProfissionalDTO detalhesProfissionalAutenticado() {
        Profissional profissional = getAuthenticatedProfissional();
        if (profissional != null) {
            return new ProfissionalDTO(
                    profissional.getId(),
                    profissional.getNome(),
                    profissional.getTelefone(),
                    profissional.getEndereco(),
                    profissional.getUsername(),
                    profissional.getProfissoes(),
                    profissional.getEspecialidades(),
                    profissional.getDisponibilidade()
            );
        } else {
            throw new RuntimeException("Nenhum profissional autenticado encontrado");
        }
    }

    public List<ServicoDTO> buscarServicosDoProfissional(Long id) {
        Profissional profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado com o ID: " + id));

        return servicoRepository.findByProfissional(profissional).stream()
                .map(servico -> new ServicoDTO(servico.getId(), servico.getDescricaoDoServico()))
                .collect(Collectors.toList());
    }
    public List<ProfissionalDTO> listarProfissionaisPorProfissao(Profissoes profissoes) {
        List<Profissional> profissionais = profissionalRepository.findByProfissoes(profissoes);
        return profissionais.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private ProfissionalDTO convertToDTO(Profissional profissional) {
        return new ProfissionalDTO(
                profissional.getId(),
                profissional.getNome(),
                profissional.getTelefone(),
                profissional.getEndereco(),
                profissional.getUsername(),
                profissional.getProfissoes(),
                profissional.getEspecialidades(), profissional.getDisponibilidade()
        );
    }
    public void atualizarDisponibilidade(Long idProfissional, boolean novaDisponibilidade) {
        Profissional profissional = profissionalRepository.findById(idProfissional)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado com o ID: " + idProfissional));

        profissional.setDisponibilidade(novaDisponibilidade);

        profissionalRepository.save(profissional);
    }

    public List<Avaliacao> listarAvaliacoesDoUsuarioAutenticado() {
        // Obter o profissional autenticado
        Profissional profissional = getAuthenticatedProfissional();
        if (profissional == null) {
            // Se o profissional não estiver autenticado, retornar uma lista vazia ou lançar uma exceção, dependendo do seu caso de uso
            return Collections.emptyList();
        }

        // Buscar os serviços do profissional autenticado
        List<Servico> servicos = servicoRepository.findByProfissionalId(profissional.getId());

        // Mapear cada serviço para uma String contendo a descrição do serviço e retornar como lista
        List<String> descricoesServicos = servicos.stream()
                .map(Servico::getDescricaoDoServico)
                .collect(Collectors.toList());

        List<Pedido> pedidos = servicos.stream()
                .map(Servico::getPedidos)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return pedidos.stream()
                .map(Pedido::getAvaliacoes)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Profissional getAuthenticatedProfissional() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Profissional) {
            return (Profissional) authentication.getPrincipal();
        }
        return null;
    }

}
