package it.dgspa.skeleton.dal;

import it.dgspa.skeleton.entity.Player;

import java.util.List;

public interface PlayerDAL {

    List<Player> playerList();

    Player getPlayer(String playerId);

    Player addNewPlayer(Player player);

    //TODO METODI PER ELABORAZIONE PLAYER

}
