package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
    Profissional findByUsername(String username);
    boolean existsByUsername(String username);
    void deleteById(Long id);
    Optional<Profissional> findById(Long id);
    Profissional findById(long id);


}
