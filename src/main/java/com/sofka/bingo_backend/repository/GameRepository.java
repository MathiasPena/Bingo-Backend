package com.sofka.bingo_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.sofka.bingo_backend.model.Game;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    Game findFirstByStatus(Game.GameStatus status);
}


