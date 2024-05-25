package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.controller;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.AvaliacaoResponseDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ServicoDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Servico;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ServicoRepository;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services.ProfissionalService;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services.ServicoService;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ProfissionalService profissionalService;
    @Autowired
    private ServicoService servicoService;

    @Autowired
    private ServicoRepository servicoRepository;

    @PostMapping("/adicionar")
    public ResponseEntity<Servico> adicionarServico(@RequestBody ServicoDTO servicoDTO) {
        try {
            Servico novoServico = servicoService.adicionarServico(servicoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoServico);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirServico(@PathVariable Long id) {
        try {
            servicoService.excluirServico(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/meus")
    public ResponseEntity<List<ServicoDTO>> listarServicos() {
        List<ServicoDTO> servicos = servicoService.listarServicosDoProfissionalAutenticado();
        return ResponseEntity.ok(servicos);
    }

   @GetMapping("/listar")
    public ResponseEntity<List<Servico>> listarTodosServicos() {
        List<Servico> servicos = servicoService.listarTodosServicos();
        return ResponseEntity.ok(servicos);
    }
    @GetMapping("/lista/{idProfissional}")
    public ResponseEntity<List<ServicoDTO>> listarServicosDoProfissional(@PathVariable Long idProfissional) {
        var profissional = profissionalService.buscarPorId(idProfissional);
        if (profissional != null) {
            List<Servico> servicos = servicoRepository.findByProfissional(profissional);
            List<ServicoDTO> servicosDTO = servicos.stream()
                    .map(servico -> new ServicoDTO(servico.getId(), servico.getDescricaoDoServico()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(servicosDTO);
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/avaliacoes")
    public ResponseEntity<List<AvaliacaoResponseDTO>> getAvaliacoesDoProfissional() {
        List<AvaliacaoResponseDTO> avaliacoes = servicoService.listarAvaliacoesDoProfissional();
        if (avaliacoes != null && !avaliacoes.isEmpty()) {
            return ResponseEntity.ok(avaliacoes);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
