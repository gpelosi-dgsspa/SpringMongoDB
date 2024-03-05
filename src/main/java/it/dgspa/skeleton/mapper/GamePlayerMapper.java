package it.dgspa.skeleton.mapper;

import it.dgspa.skeleton.dto.GamePlayerDTO;
import it.dgspa.skeleton.dto.PlayerDTO;
import it.dgspa.skeleton.dto.StartGameDTO;
import it.dgspa.skeleton.dto.EndGameDTO;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;
import it.dgspa.skeleton.entity.Ranking;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GamePlayerMapper {

    public Game fromEndGameDTOtoGameEntity(EndGameDTO endGameDTO) throws Exception{
        Game changedGame = new Game();
        //TO-DO aggiungere verifica su gameId, oppure dare per assodato che sia già stato verificato
        changedGame.setIdGame(endGameDTO.getGameId());
        changedGame.setGameStatus(endGameDTO.getGameStatus());
        changedGame.setLastupdateDate(endGameDTO.getUpdateDate());
        List<String> nicknames = new ArrayList<>();
        int findWinner = 0;
        int totalGameScore = 0;
        for(GamePlayerDTO gamePlayerDTO : endGameDTO.getGamePlayers()){

            //TO-DO aggiungere controllo che il nickname esista
            nicknames.add(gamePlayerDTO.getNickname());
            totalGameScore += gamePlayerDTO.getScore();
            Ranking ranking = new Ranking();
            ranking.setIdGame(endGameDTO.getGameId());
            ranking.setIdPlayer(gamePlayerDTO.getNickname());
            ranking.setGameScore(gamePlayerDTO.getScore());

            if(gamePlayerDTO.getWinner()){
                findWinner++;
                ranking.setWinner(true);
            } else {
                ranking.setWinner(false);
            }
        }
        changedGame.setNicknamePlayers(nicknames);
        changedGame.setTotalScore(totalGameScore);

        if(findWinner>1){
            throw new Exception("Find more than one winner!");
        }
        return changedGame;
    }

    public Game fromStartGameDTOtoGameEntity(StartGameDTO startGameDTO){
        Game newGame = new Game();
        newGame.setGameLevel(startGameDTO.getLevel());
        newGame.setGameStatus(startGameDTO.getGameStatus());
        newGame.setLastupdateDate(startGameDTO.getStartDate());
        newGame.setNicknamePlayers(startGameDTO.getGamePlayers());
        return newGame;
    }

    public Player fromPlayerDTOtoPlayerEntity(PlayerDTO playerDTO){
        Player newPlayer = new Player();
        newPlayer.setPlayerStatus(playerDTO.getPlayerStatus());
        newPlayer.setEta(playerDTO.getEta());
        newPlayer.setNickname(playerDTO.getNickname());
        return newPlayer;
    }
}
