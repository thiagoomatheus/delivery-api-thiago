package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.request.LoginRequest;
import com.deliverytech.delivery_api.dto.request.RegisterRequest;
import com.deliverytech.delivery_api.model.Role;
import com.deliverytech.delivery_api.model.Usuario;
import com.deliverytech.delivery_api.repository.UsuarioRepository;
import com.deliverytech.delivery_api.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "Auth", description = "Endpoints para autenticação e registro de usuários")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(
        summary = "Registrar um novo usuário",
        description = "Cria um novo usuário com as informações fornecidas. O email deve ser único."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Email já cadastrado ou dados inválidos")
    })
    public ResponseEntity<String> register(
        @Parameter(
            description = "Informações do novo usuário",
            example = "{\"email\": \"joao@email\", \"senha\": \"123456\", \"nome\": \"João\", \"role\": \"CLIENTE\", \"restauranteId\": 1}",
            required = true
        )
        @Valid @RequestBody RegisterRequest request
    ) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .nome(request.getNome())
                .role(request.getRole() != null ? request.getRole() : Role.CLIENTE)
                .ativo(true)
                .restauranteId(request.getRestauranteId())
                .build();

        usuarioRepository.save(usuario);
        String token = jwtUtil.generateToken(User.withUsername(usuario.getEmail()).password(usuario.getSenha()).authorities("ROLE_" + usuario.getRole().name()).build(), usuario);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/login")
    @Operation(
        summary = "Efetuar login",
        description = "Realiza a autenticação do usuário com email e senha. Retorna um token JWT se as credenciais forem válidas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login bem-sucedido, token retornado"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    public ResponseEntity<String> login(
        @Parameter(
            description = "Credenciais do usuário",
            example = "{\"email\": \"joao@email\", \"senha\": \"123456\"}",
            required = true
        )
        @Valid @RequestBody LoginRequest request
        ) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        String token = jwtUtil.generateToken(User.withUsername(usuario.getEmail()).password(usuario.getSenha()).authorities("ROLE_" + usuario.getRole().name()).build(), usuario);
        return ResponseEntity.ok(token);
    }
}
