package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.UserModel;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>{
    UserDetails findByUsername(String username);
    boolean existsByUsername(String username);
    
}
