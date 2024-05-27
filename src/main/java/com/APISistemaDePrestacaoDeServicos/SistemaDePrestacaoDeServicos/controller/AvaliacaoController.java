package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.controller;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.AvaliacaoDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.AvaliacaoResponseDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Avaliacao;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services.AvaliacaoService;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services.PedidoService;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private AvaliacaoService avaliacaoService;
    @Autowired
    private ServicoService servicoService;

    @PostMapping("/avaliar")
    public ResponseEntity<AvaliacaoResponseDTO> avaliarServico(@RequestBody AvaliacaoDTO avaliacaoDTO) {
        Avaliacao avaliacao = avaliacaoService.avaliarServico(
                avaliacaoDTO.pedidoId(),
                avaliacaoDTO.nota(),
                avaliacaoDTO.comentario()
        );
        AvaliacaoResponseDTO responseDTO = new AvaliacaoResponseDTO(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getPedido().getId()
        );
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/profissional/{profissionalId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarAvaliacoesPorProfissional(@PathVariable Long profissionalId) {
        List<Avaliacao> avaliacoes = avaliacaoService.listarAvaliacoesPorProfissional(profissionalId);
        List<AvaliacaoResponseDTO> responseDTOs = avaliacoes.stream()
                .map(avaliacao -> new AvaliacaoResponseDTO(
                        avaliacao.getId(),
                        avaliacao.getNota(),
                        avaliacao.getComentario(),
                        avaliacao.getPedido().getId()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }
    @GetMapping("/servicos/{servicoId}")
    public ResponseEntity<List<AvaliacaoResponseDTO>> listarAvaliacoesDoServico(@PathVariable Long servicoId) {
        try {
            List<AvaliacaoResponseDTO> avaliacoes = servicoService.listarAvaliacoesDoServico(servicoId);
            return ResponseEntity.ok(avaliacoes);
        } catch (IllegalArgumentException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }


    }


}
