package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.Profissoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    Profissional findByUsername(String username);
    boolean existsByUsername(String username);
    void deleteById(Long id);
    Optional<Profissional> findById(Long id); // Mantém apenas este método
    List<Profissional> findByProfissoes(Profissoes profissoes);


}
