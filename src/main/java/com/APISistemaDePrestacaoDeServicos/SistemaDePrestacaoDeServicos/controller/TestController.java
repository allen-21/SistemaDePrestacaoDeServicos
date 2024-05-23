package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @GetMapping("/test")
    public String testConnection() {
        return "Conexão entre Spring Boot e Flutter bem-sucedida!";
    }
}
