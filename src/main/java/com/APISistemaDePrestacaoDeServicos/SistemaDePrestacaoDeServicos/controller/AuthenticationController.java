package com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.controller;


import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.AuthenticationDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.LoginResponseDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.dtos.RegisterDTO;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.infra.security.TokenService;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.Administrador;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.models.UserModel;
import com.APISistemaDePrestacaoDeServicos.SistemaDePrestacaoDeServicos.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("auth")
@Validated
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated AuthenticationDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((UserModel) auth.getPrincipal());


        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @GetMapping("/user")
    public String getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String role = authentication.getAuthorities().iterator().next().getAuthority();

        String jsonResponse = "{\"role\": \"" + role + "\"}";

        return jsonResponse;
    }




    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if(this.userRepository.findByUsername(data.username()) !=null)return ResponseEntity.badRequest().build();

        String encryptePassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel newUser = new Administrador(data.username(), encryptePassword, data.role());
        this.userRepository.save(newUser);
        return  ResponseEntity.ok().build();
    }



}
