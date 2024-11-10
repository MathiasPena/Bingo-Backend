package com.sofka.bingo_backend.service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.sofka.bingo_backend.model.Game;
import com.sofka.bingo_backend.model.Player;
import com.sofka.bingo_backend.repository.GameRepository;
import com.sofka.bingo_backend.repository.PlayerRepository;

import jakarta.annotation.PostConstruct;

@Service
public class GameService {

    private GameRepository gameRepository;
    private PlayerRepository playerRepository;

    // Constructor para inyectar los repositorios necesarios
    public GameService(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    // Método que se ejecuta al iniciar la aplicación para crear una partida por
    // defecto
    @PostConstruct
    public void crearPartidaPorDefecto() {

        // Elimina todos los juegos y jugadores existentes en la base de datos
        gameRepository.deleteAll();
        playerRepository.deleteAll();

        // Busca si ya existe una partida en estado de espera
        Game partidaEnEspera = gameRepository.findFirstByStatus(Game.GameStatus.WAITING);

        // Si no existe una partida en espera, crea una nueva
        if (partidaEnEspera == null) {
            Game game = new Game(new ArrayList<>(), Game.GameStatus.WAITING);
            game.setId(UUID.randomUUID().toString()); // Asigna un ID único a la nueva partida
            gameRepository.save(game); // Guarda la nueva partida en la base de datos
        }
    }

    // Método para asignar un jugador a una partida en espera
    public Optional<Game> asignarJugadorAPartida(String authToken) {
        // Busca al jugador por su token de autenticación
        Optional<Player> playerOpt = playerRepository.findByToken(authToken);
        if (playerOpt.isEmpty())
            return Optional.empty(); // Si no se encuentra al jugador, devuelve un Optional vacío

        Player player = playerOpt.get();

        // Busca una partida en estado 'WAITING'
        Optional<Game> gameOpt = Optional.ofNullable(gameRepository.findFirstByStatus(Game.GameStatus.WAITING));
        if (gameOpt.isEmpty())
            return Optional.empty(); // Si no se encuentra una partida en espera, devuelve un Optional vacío

        Game game = gameOpt.get();
        game.addPlayer(player); // Añade el jugador a la partida
        player.setStatus("jugando"); // Actualiza el estado del jugador a 'jugando'

        // Si se ha alcanzado el número máximo de jugadores, cambia el estado de la
        // partida a 'IN_PROGRESS'
        if (game.getPlayers().size() == 1) { // Ajusta a tu lógica de maxJugadores
            game.setStatus(Game.GameStatus.IN_PROGRESS); // Cambia el estado a 'IN_PROGRESS'
        }

        // Guarda la partida y el jugador actualizados en la base de datos
        gameRepository.save(game);
        playerRepository.save(player);

        return Optional.of(game); // Devuelve la partida con el jugador asignado
    }
}
