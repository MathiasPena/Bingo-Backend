package com.sofka.bingo_backend.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.sofka.bingo_backend.dto.LoginRequest;
import com.sofka.bingo_backend.exceptions.InvalidCredentialsException;
import com.sofka.bingo_backend.exceptions.UserNotFoundException;
import com.sofka.bingo_backend.model.AuthUser;
import com.sofka.bingo_backend.model.Player;
import com.sofka.bingo_backend.repository.PlayerRepository;
import com.sofka.bingo_backend.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository usuarioRepository;
    private final PlayerRepository playerRepository;

    // Constructor de servicio
    public AuthService(UserRepository usuarioRepository, PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new UserNotFoundException("Username cannot be null or empty");
        }

        // Busca el usuario por nombre y lo devuelve con las credenciales
        return usuarioRepository.findByUsername(username)
                .map(usuario -> new User(usuario.getUsername(), usuario.getPassword(), getAuthorities()))
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    // Establece los permisos para el usuario (en este caso, solo "Jugador")
    private List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("Jugador"));
        return authorities;
    }

    // Autentica al usuario y devuelve un token
    public String authenticateUser(LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            throw new InvalidCredentialsException();
        }

        // Verifica si el usuario existe y si la contraseña es correcta
        AuthUser usuario = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!loginRequest.getPassword().equals(usuario.getPassword())) {
            throw new InvalidCredentialsException();
        }

        // Verifica si el jugador tiene una cuenta y, si no, la crea
        Player jugador = playerRepository.findByUsername(usuario.getUsername());

        String authToken = generateToken();

        if (jugador == null) {
            jugador = new Player(authToken, "Esperando");
            playerRepository.save(jugador);
        }

        // Asigna el token de autenticación al usuario y lo guarda
        usuario.setAuthToken(authToken);
        usuarioRepository.save(usuario);

        return authToken;
    }

    // Genera un token único para la sesión del usuario
    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    // Obtiene el jugador basado en el token de autenticación
    public Optional<Player> obtenerJugadorPorToken(String authToken) {
        return playerRepository.findByToken(authToken);
    }
}
