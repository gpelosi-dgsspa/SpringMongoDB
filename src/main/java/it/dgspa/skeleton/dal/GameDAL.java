package it.dgspa.skeleton.dal;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;

import java.util.List;

public interface GameDAL {

    //recupera tutte le partite
    List<Game>  getAllGames();

    List<Game> findGamesById(String id);

    Game addNewGame(Game g);
    List<Game> getAllGamesPlayedByPlayer(String idPlayer);


    public Integer getAllGamesLooseByPlayer(Player p);

    public Integer getCountAllGamesPlayedByPlayer(String idPlayer);
    public Integer getAllGamesWinByPlayer(String idPlayer);

    public List<Game> getAllWinGames(Player p);


}
