package com.sofka.bingo_backend.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameLogicService {

    private final SimpMessagingTemplate messagingTemplate;
    private static final Logger logger = LogManager.getLogger(GameLogicService.class);

    // Inyección de dependencias
    public GameLogicService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    Random random = new Random();

    // Genera un cartón de bingo con 25 números únicos
    public List<Integer> generateBingoCard() {
        Set<Integer> cardSet = new HashSet<>();
        List<Integer> card = new ArrayList<>();

        // Genera números aleatorios hasta tener 25 únicos
        while (cardSet.size() < 25) {
            cardSet.add(random.nextInt(75) + 1);
        }
        card.addAll(cardSet);
        return card;
    }

    // Inicia el sorteo de balotas
    public void startBallDraw() {
        // Espera inicial de 5 segundos antes de comenzar el sorteo
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Error al esperar", e);
        }

        // Crea una lista con los números del bingo (1 a 75)
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 75; i++) {
            numbers.add(i);
        }

        // Baraja los números para simular el sorteo
        Collections.shuffle(numbers);

        // Inicia el sorteo en un hilo separado
        new Thread(() -> {
            for (Integer number : numbers) {
                try {
                    Thread.sleep(5000); // Espera 5 segundos entre bolas
                    messagingTemplate.convertAndSend("/topic/numbers", number); // Envía el número sorteado
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.error("Error en el sorteo", e);
                }
            }
        }).start();
    }
}
