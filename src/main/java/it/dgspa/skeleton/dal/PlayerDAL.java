package it.dgspa.skeleton.dal;

import it.dgspa.skeleton.dto.PlayerDto;
import it.dgspa.skeleton.entity.Player;

import java.util.List;

public interface PlayerDAL {

    List<Player> playerList();

    Player getPlayer(String nickname);

    Player addNewPlayer(PlayerDto player);

    //TODO METODI PER ELABORAZIONE PLAYER

}
