package com.sofka.bingo_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "partidas")
public class Game {

    @Id
    private String id;
    private int authUserId;
    private List<Player> players; // Lista de jugadores
    private GameStatus status; // Estado de la partida
    private List<String> playerCards; // Las tarjetas de los jugadores (cada una es un String de 75 números)
    private String winnerId; // El ganador de la partida

    public int getAuthUserId() {
        return authUserId;
    }

    public void setAuthUserId(int authUserId) {
        this.authUserId = authUserId;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    // Constructor, getters y setters
    public Game(List<Player> players, GameStatus status) {
        this.players = players;
        this.status = status;
        this.playerCards = new ArrayList<>();
        this.winnerId = null;
    }

    // Enum para el estado de la partida
    public enum GameStatus {
        WAITING, // La partida está esperando jugadores
        IN_PROGRESS, // La partida está en curso
        FINALIZED // La partida ha terminado
    }

    public void addPlayer(Player player) {
        if (players == null) {
            players = new ArrayList<>();
        }
        players.add(player);
    }

    public Game() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public List<String> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(List<String> playerCards) {
        this.playerCards = playerCards;
    }

    public void setWinner(String winner) {
        this.winnerId = winner;
    }
}
