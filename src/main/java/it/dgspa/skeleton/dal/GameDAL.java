package it.dgspa.skeleton.dal;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface GameDAL {

    //recupera tutte le partite
    List<Game>  getAllGames();

    List<Game> findGamesById(String id);

    ResponseEntity<Object> startNewGame(Boolean insert);

    public   ResponseEntity<Object> closegame (String idGame);
    public  ResponseEntity<List<Game>> getAllGamesPlayedByPlayer(String nickname);
    public ResponseEntity<Integer> countAllGamesPlayerLosses(String nickname);
    public ResponseEntity<Integer> getCountAllGamesPlayedByPlayer(String  nickname);
    public ResponseEntity<Integer> countAllGamesPlayerWins(String  nickname);
    public ResponseEntity<List<Game>> getAllWinGames(String nickname);
    public  ResponseEntity<List<Game>> getAllLostGames(String nickname);


}