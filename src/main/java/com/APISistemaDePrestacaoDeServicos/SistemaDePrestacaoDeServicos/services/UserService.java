package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.services;



import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.UserModel;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.UserRepository;


@Service
public class UserService {

    private final UserRepository userRepository;


    public UserModel getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserModel) {
            return (UserModel) authentication.getPrincipal();
        }
        return null;
    }

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public boolean existsByUsername(String username) {

        return userRepository.existsByUsername(username);
    }
    
}
