package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.controller;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.RegisterClienteDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Cliente;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ClienteRepository;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
@Validated
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping("/criar")
    public ResponseEntity<?> criarCliente(@Valid @RequestBody RegisterClienteDTO clienteDTO) {
      try {
          Cliente cliente = clienteService.salvarCliente(clienteDTO);
          return new ResponseEntity<>(cliente, HttpStatus.CREATED);
      } catch (IllegalArgumentException e){
          return ResponseEntity.badRequest().body("Nome de usuário já está em uso: " + clienteDTO.username());
      }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Cliente>> listarTodosClientes() {
        List<Cliente> clientes = clienteService.listarTodosClientes();
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/buscar/{username}")
    public ResponseEntity<?> buscarClientePorUsername(@PathVariable String username) {
        try {
            Cliente cliente = clienteService.buscarPorUsername(username);
            if (cliente != null) {
                return ResponseEntity.ok(cliente);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }




}
