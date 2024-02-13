package it.dgspa.skeleton.repository;

import it.dgspa.skeleton.entity.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, String> {
    
}
