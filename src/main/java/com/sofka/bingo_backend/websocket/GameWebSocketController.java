package com.sofka.bingo_backend.websocket;

import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.sofka.bingo_backend.model.Game;
import com.sofka.bingo_backend.model.Player;
import com.sofka.bingo_backend.repository.GameRepository;
import com.sofka.bingo_backend.repository.PlayerRepository;
import com.sofka.bingo_backend.service.GameLogicService;

@Controller
public class GameWebSocketController {

    private final GameRepository gameRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final PlayerRepository playerRepository;
    private final GameLogicService gameLogicService;
    private static final Logger logger = LogManager.getLogger(GameWebSocketController.class);

    // Constructor para inyectar las dependencias necesarias
    public GameWebSocketController(GameRepository gameRepository, SimpMessagingTemplate messagingTemplate,
            PlayerRepository playerRepository, GameLogicService gameLogicService) {
        this.messagingTemplate = messagingTemplate;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.gameLogicService = gameLogicService;
    }

    // Mapea el mensaje de unión al juego y envía actualizaciones al canal
    // correspondiente
    @MessageMapping("/joinGame")
    @SendTo("/topic/gameUpdates")
    public Game joinGame(String authToken) {
        logger.info("Intento de unión al juego con token: {}", authToken);

        // Busca al jugador en la base de datos usando el token de autenticación
        Optional<Player> playerOpt = playerRepository.findByToken(authToken);
        if (playerOpt.isPresent()) {
            logger.info("Token válido. Jugador encontrado en la base de datos.");

            Player player = playerOpt.get();

            // Busca un juego en progreso
            Game existingGame = gameRepository.findFirstByStatus(Game.GameStatus.IN_PROGRESS);
            if (existingGame != null) {
                // Genera un cartón de bingo para el jugador
                List<Integer> playerCard = gameLogicService.generateBingoCard();
                player.setCard(playerCard.toString());
                playerRepository.save(player);

                // Añade el cartón del jugador al juego y lo guarda en la base de datos
                existingGame.getPlayerCards().add(playerCard.toString());
                gameRepository.save(existingGame);

                // Envía la actualización del juego al jugador a través de WebSocket
                messagingTemplate.convertAndSendToUser(authToken, "/topic/gameUpdates", existingGame);
                logger.info("Jugador unido exitosamente al juego. Cartón asignado y juego actualizado.");

                // Inicia el sorteo de balotas
                gameLogicService.startBallDraw();

                return existingGame;
            } else {
                // Si no hay un juego en progreso, informa al jugador
                messagingTemplate.convertAndSendToUser(authToken, "/topic/gameStatus",
                        "No se encontró ningún juego en progreso");
            }
        } else {
            logger.info("Token inválido.");
        }
        return null;
    }
}
