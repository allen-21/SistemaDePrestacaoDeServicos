package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Profissional;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums.Profissoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
@Repository



public interface ProfissionalRepository extends JpaRepository<Profissional, Long>, JpaSpecificationExecutor<Profissional> {
    Profissional findByUsername(String username);
    boolean existsByUsername(String username);
    void deleteById(Long id);
    Optional<Profissional> findById(Long id);
    List<Profissional> findByProfissoes(Profissoes profissoes);

    default List<Profissional> findByNomeOuProfissao(String termo) {
        return findAll(Specification.where(containsNome(termo)).or(containsProfissao(termo)));
    }

    static Specification<Profissional> containsNome(String termo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("nome"), "%" + termo + "%");
    }

    static Specification<Profissional> containsProfissao(String termo) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("profissoes"), "%" + termo + "%");
    }
}
