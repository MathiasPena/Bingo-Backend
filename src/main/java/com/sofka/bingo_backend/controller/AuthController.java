package com.sofka.bingo_backend.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sofka.bingo_backend.dto.LoginRequest;
import com.sofka.bingo_backend.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService; // Servicio para manejar la autenticación

    // Constructor del controlador
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint para login de usuarios
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        // Autentica al usuario y obtiene el token
        String token = authService.authenticateUser(loginRequest);

        // Prepara la respuesta con el token
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response); // Retorna el token como respuesta
    }
}
