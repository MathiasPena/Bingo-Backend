package com.sofka.bingo_backend.controller;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sofka.bingo_backend.model.Game;
import com.sofka.bingo_backend.service.GameService;

@RestController
@RequestMapping("/game")
public class GameController {

    private GameService gameService; // Servicio para gestionar las partidas

    // Constructor del controlador
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // Endpoint de prueba, requiere que el usuario tenga el rol 'Jugador'
    @PostMapping("/test")
    @PreAuthorize("hasAuthority('Jugador')") // Asegura que solo los jugadores autenticados puedan acceder
    public String testEndpointPost(@RequestHeader("Authorization") String token) {
        return "Token: " + token;
    }

    // Endpoint para unirse a una partida
    @PostMapping("/join")
    public ResponseEntity<Game> unirseAPartida(@RequestHeader("Authorization") String authToken) {
        try {
            // Intenta asignar al jugador a una partida
            Optional<Game> gameOpt = gameService.asignarJugadorAPartida(authToken);

            if (gameOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Game()); // Si no hay partida disponible
            }

            return ResponseEntity.ok(gameOpt.get()); // Retorna la partida asignada
        } catch (Exception e) {
            // Loguea el error para saber qué ocurrió
            e.printStackTrace(); // Idealmente usar un logger aquí
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Game()); // Responde con un Game vacío en caso de error
        }
    }
}
