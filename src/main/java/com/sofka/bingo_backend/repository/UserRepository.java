package com.sofka.bingo_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.sofka.bingo_backend.model.AuthUser;
import java.util.Optional;

public interface UserRepository extends MongoRepository<AuthUser, String> {
    Optional<AuthUser> findByUsername(String username);
}
