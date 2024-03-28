package it.dgspa.skeleton.dal;
import it.dgspa.skeleton.dto.PlayerInsertDto;
import it.dgspa.skeleton.entity.Player;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlayerDAL {

    List<Player> playerList();

    ResponseEntity<Object> getPlayer(String nickname);

    ResponseEntity<Object> addNewPlayer(PlayerInsertDto player);

    //TODO METODI PER ELABORAZIONE PLAYER

}
