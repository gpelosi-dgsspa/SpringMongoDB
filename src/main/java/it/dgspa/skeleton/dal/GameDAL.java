package it.dgspa.skeleton.dal;
import it.dgspa.skeleton.dto.GameDto;
import it.dgspa.skeleton.dto.GamePlayerDto;
import it.dgspa.skeleton.dto.PlayerDto;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface GameDAL {

    //recupera tutte le partite
    List<Game>  getAllGames();

    List<Game> findGamesById(String idGame);

   // ResponseEntity<Object> startNewGame(List<PlayerDto> players);

    public   ResponseEntity<Object> closegame (GameDto gameDto);
    public  ResponseEntity<List<Game>> getAllGamesPlayedByPlayer(String nickname);
    public ResponseEntity<Integer> countAllGamesPlayerLosses(String nickname);
    public ResponseEntity<Integer> getCountAllGamesPlayedByPlayer(String  nickname);
    public ResponseEntity<Integer> countAllGamesPlayerWins(String  nickname);
    public ResponseEntity<List<Game>> getAllWinGames(String nickname);
    public  ResponseEntity<List<Game>> getAllLostGames(String nickname);


}