package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ProfissionalDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ProfissionalUpdateDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.RegisterProfissionalDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ServicoDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.Profissoes;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ProfissionalRepository;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        // Verificar se o profissional existe
        Profissional profissional = profissionalRepository.findById(idProfissional)
                .orElseThrow(() -> new RuntimeException("Profissional não encontrado com o ID: " + idProfissional));

        // Atualizar a disponibilidade
        profissional.setDisponibilidade(novaDisponibilidade);

        // Salvar as mudanças no banco de dados
        profissionalRepository.save(profissional);
    }

    public Profissional getAuthenticatedProfissional() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Profissional) {
            return (Profissional) authentication.getPrincipal();
        }
        return null;
    }

}
