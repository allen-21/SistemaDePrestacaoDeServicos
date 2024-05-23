package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
}
