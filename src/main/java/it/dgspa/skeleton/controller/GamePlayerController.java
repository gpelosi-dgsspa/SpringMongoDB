package it.dgspa.skeleton.controller;

import it.dgspa.skeleton.enumeration.GameStatus;
import it.dgspa.skeleton.enumeration.PlayerStatus;
import it.dgspa.skeleton.dto.*;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;
import it.dgspa.skeleton.service.GamePlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value="/game_player")
public class GamePlayerController {

    @Autowired
    private GamePlayerService gamePlayerService;

    @GetMapping(value = "/allPlayers")
    public ResponseEntity<List<Player>> getAllPlayers(){
        return gamePlayerService.allPlayers();
    }

    @PostMapping(value = "/createPlayer")
    public ResponseEntity<Object> createPlayer(@RequestBody PlayerDTO playerDTO){
        playerDTO.setPlayerStatus(PlayerStatus.ACTIVE);
        return gamePlayerService.addOrUpdatePlayer(playerDTO, true);
    }

    @PutMapping(value = "/deletePlayer")
    public ResponseEntity<Object> deletePlayer(@RequestBody PlayerDTO playerDTO){
        playerDTO.setPlayerStatus(PlayerStatus.DELETED);
        return gamePlayerService.addOrUpdatePlayer(playerDTO, false);
    }


    @GetMapping(value = "/allGames")
    public ResponseEntity<List<Game>> getAllGames(){
        return gamePlayerService.allGames();
    }

    @PostMapping(value = "/createGame")
    public ResponseEntity<Game> createGame(@RequestBody StartGameDTO startGame){
        startGame.setStartDate(new Date());
        startGame.setGameStatus(GameStatus.INIZIATA);
        return gamePlayerService.startGame(startGame);
    }

    @PostMapping(value = "/terminateGame")
    public ResponseEntity<Object> terminateGame(@RequestBody EndGameDTO endGame){
        endGame.setEndDate(new Date());
        endGame.setGameStatus(GameStatus.TERMINATA);
        return gamePlayerService.closeGame(endGame);
    }


    @GetMapping(value = "/bestScoreLastFiveGames")
    public ResponseEntity<List<BestLastGamesDTO>> bestScoreLastFiveGames(){
        return gamePlayerService.getBestScoreLast5Games();
    }

    @GetMapping(value = "/bestWinner")
    public ResponseEntity<List<BestWinRankDTO>> getBestWinnerRank(){
        //Ritorna il nome del giocatore che in assoluto ha vinto più partite
        return gamePlayerService.getBestWinnerRank();
    }

    @GetMapping(value = "/bestGameScore")
    public ResponseEntity<List<BestScoreRankDTO>> getBestScoreRank(){
        //Ritorna il nome del giocatore e il punteggio del migliore risultato nelle ultime 5 partite
        return gamePlayerService.getBestScoreRank();
    }

    @DeleteMapping(value = "/dropAllData")
    public ResponseEntity<String> dropAllData(){
        return gamePlayerService.dropAllData();
    }

}
