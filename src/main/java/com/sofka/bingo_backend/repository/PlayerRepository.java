package com.sofka.bingo_backend.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.sofka.bingo_backend.model.Player;

@Repository
public interface PlayerRepository extends MongoRepository<Player, String> {
    Player findByUsername(String username);
    Optional<Player> findByToken(String token);
}

