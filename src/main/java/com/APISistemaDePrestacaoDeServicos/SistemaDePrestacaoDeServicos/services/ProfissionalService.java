package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.RegisterProfissionalDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ServicoDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
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



    public Profissional buscarPorUsername(String username) {
        return profissionalRepository.findByUsername(username);
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
    public Profissional buscarPorId(Long id){
        Optional<Profissional> profissional = profissionalRepository.findById(id);
        return profissional.orElse(null);
    }
    public List<ServicoDTO> buscarServicosDoProfissional(Long id) {
        Profissional profissional = profissionalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado com o ID: " + id));

        return servicoRepository.findByProfissional(profissional).stream()
                .map(servico -> new ServicoDTO(servico.getId(), servico.getDescricaoDoServico()))
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
