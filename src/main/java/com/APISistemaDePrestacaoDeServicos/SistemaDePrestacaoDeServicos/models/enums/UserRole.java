package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"),
    CLIENTE("ROLE_CLIENTE"),
    PROFISSIONAL("ROLE_PROF");
 
    private String role;

   UserRole (String role){
        this.role = role;
    }

}
