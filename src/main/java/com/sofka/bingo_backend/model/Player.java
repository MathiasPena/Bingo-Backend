package com.sofka.bingo_backend.model;

// Clase Jugador (hereda de Usuario)
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jugadores")
public class Player extends AuthUser {

    private String token;
    private String status; // Estado del jugador
    private String card;  // Tarjeta del jugador
    private boolean[] markedBalls; // Balotas marcadas por el jugador

    // Constructor, getters y setters
    public Player(String token, String status) {
        super();
        this.token = token;
        this.status = status;
        this.card = ""; // Inicializa la carta
        this.markedBalls = new boolean[75];
    }

    

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public boolean[] getMarkedBalls() {
        return markedBalls;
    }

    public void setMarkedBalls(boolean[] markedBalls) {
        this.markedBalls = markedBalls;
    }
    
    public void markBall(int ballNumber) {
        // Marca la balota correspondiente en el array de balotas
        if (ballNumber >= 1 && ballNumber <= 75) {
            markedBalls[ballNumber - 1] = true;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getToken() {
        return token;
    }



    public void setToken(String token) {
        this.token = token;
    }
}

