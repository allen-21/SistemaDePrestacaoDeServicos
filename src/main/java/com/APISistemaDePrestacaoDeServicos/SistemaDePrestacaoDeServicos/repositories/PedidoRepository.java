package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Cliente;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Pedido;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByServicoId(Long servicoId);
    List<Pedido> findByServico_Profissional(Profissional profissional);
    List<Pedido> findByCliente(Cliente cliente);

}
