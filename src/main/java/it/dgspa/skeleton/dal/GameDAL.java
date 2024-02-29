package it.dgspa.skeleton.dal;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;

import java.util.ArrayList;
import java.util.List;

public interface GameDAL {

    //recupera tutte le partite
    List<Game>  getAllGames();

    List<Game> findGamesById(String id);

    Game addNewGame(Game g);
    List<Game> getAllGamesPlayedByPlayer(String id);
    public Integer getAllGamesLooseByPlayer(String idGiocatore);
    public Integer getCountAllGamesPlayedByPlayer(String  idGiocatore);
    public Integer getAllGamesWinByPlayer(String  idGiocatore);
    public List<Game> getAllWinGames(String   idGiocatore);


}