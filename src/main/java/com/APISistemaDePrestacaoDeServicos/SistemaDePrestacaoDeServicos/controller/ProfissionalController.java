package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.controller;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ProfissionalDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ProfissionalUpdateDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.RegisterProfissionalDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Avaliacao;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.Profissoes;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ProfissionalRepository;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services.ProfissionalService;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services.ServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/profissional")
@Validated
public class ProfissionalController {

    @Autowired
    private ProfissionalService profissionalService;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ServicoService servicoService;

    @PostMapping("/criar")
    public ResponseEntity<?> criarProfissional(@Valid @RequestBody RegisterProfissionalDTO profissionalDTO) {
   try {
       Profissional profissional = profissionalService.salvarProfissional(profissionalDTO);
       return new ResponseEntity<>(profissional, HttpStatus.CREATED);
   }catch (IllegalArgumentException e){
       return ResponseEntity.badRequest().body("Nome de usuário já está em uso: " + profissionalDTO.username());
   }

    }

    @PutMapping("/atualizar")
    public ResponseEntity<String> atualizarProfissionalAutenticado(@RequestBody ProfissionalUpdateDTO profissionalDTO) {
        try {
            profissionalService.atualizarProfissionalAutenticado(profissionalDTO);
            return ResponseEntity.ok("Profissional atualizado com sucesso");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao atualizar o profissional");
        }
    }

    @GetMapping("/profissao/{profissao}")
    public ResponseEntity<List<ProfissionalDTO>> listarProfissionaisPorProfissao(@PathVariable Profissoes profissao) {
        List<ProfissionalDTO> profissionais = profissionalService.listarProfissionaisPorProfissao(profissao);
        return ResponseEntity.ok(profissionais);
    }
    @GetMapping("/detalhes")
    public ResponseEntity<ProfissionalDTO> getInformacoesProfissionalAutenticado() {
        ProfissionalDTO profissionalDTO = profissionalService.detalhesProfissionalAutenticado();
        return ResponseEntity.ok(profissionalDTO);
    }

    @GetMapping("/buscar/{termo}")
    public ResponseEntity<List<Profissional>> buscarProfissionaisPorNomeOuProfissao(@PathVariable String termo) {
        List<Profissional> profissionais = profissionalService.buscarProfissionaisPorNomeOuProfissao(termo);
        return ResponseEntity.ok(profissionais);
    }
    @PatchMapping("/disponibilidade")
    public ResponseEntity<?> atualizarDisponibilidadeProfissional(@RequestBody Map<String, Boolean> requestBody) {
        // Obtém o nome de usuário do profissional autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Busca o profissional pelo nome de usuário
        Profissional profissional = profissionalService.buscarPorUsername(username);

        // Verifica se o profissional foi encontrado
        if (profissional == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profissional não encontrado");
        }

        // Obtém a nova disponibilidade do corpo da solicitação
        Boolean novaDisponibilidade = requestBody.get("novaDisponibilidade");

        // Verifica se a novaDisponibilidade é nula
        if (novaDisponibilidade == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parâmetro 'novaDisponibilidade' ausente ou inválido");
        }

        // Atualiza a disponibilidade do profissional
        profissionalService.atualizarDisponibilidade(profissional.getId(), novaDisponibilidade);

        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirProfissionalPorID(@PathVariable Long id){
        try {
            Profissional profissional = profissionalService.buscarPorId(id);
            if (profissional != null) {
                profissionalService.excluirPorId(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/usuarioAutenticado")
    public ResponseEntity<List<Avaliacao>> listarAvaliacoesDoUsuarioAutenticado() {
        List<Avaliacao> avaliacoes = profissionalService.listarAvaliacoesDoUsuarioAutenticado();
        if (avaliacoes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(avaliacoes);
    }


}
