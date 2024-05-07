package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.controller;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.RegisterProfissionalDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ServicoDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ProfissionalRepository;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services.ProfissionalService;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services.ServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /*@GetMapping("/listar")
    public List<ProfResponse> listar(){
        List<ProfResponse> profList = profissionalRepository.findAll().stream().map(ProfResponse::new).toList();
        return profList;
    }*/

    @GetMapping("/buscar/{username}")
    public ResponseEntity<?> buscarPorUsername(@PathVariable String username) {
        try {
            Profissional profissional = profissionalService.buscarPorUsername(username);
            if (profissional != null) {
                return ResponseEntity.ok().body(profissional);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profissional não encontrado com o nome de usuário: " + username);
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

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
    @GetMapping("/{id}/servicos")
    public ResponseEntity<List<ServicoDTO>> buscarServicosDoProfissional(@PathVariable("id") Long id) {
        List<ServicoDTO> servicos = profissionalService.buscarServicosDoProfissional(id);
        return ResponseEntity.ok(servicos);
    }


}
