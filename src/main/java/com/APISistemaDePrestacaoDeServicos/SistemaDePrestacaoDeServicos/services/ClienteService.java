package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.ClienteUpdateDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.RegisterClienteDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Cliente;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
   private ClienteRepository clienteRepository;

    public Cliente salvarCliente(RegisterClienteDTO clienteDTO){
        if (clienteRepository.existsByUsername(clienteDTO.username())) {
            throw new IllegalArgumentException("Nome de usuário já está em uso: " + clienteDTO.username());
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(clienteDTO.password());
        Cliente cliente = new Cliente(clienteDTO.nome(),clienteDTO.telefone(),clienteDTO.endereco(),
                clienteDTO.username(),encryptedPassword,clienteDTO.role());
        return clienteRepository.save(cliente);
    }

    public void atualizarCliente(ClienteUpdateDTO clienteDTO) {
        Cliente clienteAutenticado = getAuthenticatedCliente();
        if (clienteAutenticado != null) {
            clienteAutenticado.setNome(clienteDTO.nome());
            clienteAutenticado.setTelefone(clienteDTO.telefone());
            clienteAutenticado.setEndereco(clienteDTO.endereco());

            // Atualizar a senha se uma nova senha for fornecida
            if (clienteDTO != null && clienteDTO.password() != null && !clienteDTO.password().isEmpty()) {
                clienteAutenticado.setPassword(new BCryptPasswordEncoder().encode(clienteDTO.password()));
            }

            // Salvar as alterações
            clienteRepository.save(clienteAutenticado);
        } else {
            throw new IllegalStateException("Nenhum cliente autenticado encontrado.");
        }
    }

    public Cliente buscarPorUsername(String username) {
        return clienteRepository.findByUsername(username);
    }

    public void excluirPorId(Long id) {
        clienteRepository.deleteById(id);
    }


    public Cliente buscarPorId(Long id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        return clienteOptional.orElse(null);
    }



    public List<Cliente> listarTodosClientes() {
        return clienteRepository.findAll();
    }


    public Cliente getAuthenticatedCliente() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Cliente) {
            return (Cliente) authentication.getPrincipal();
        }
        return null;
    }


}