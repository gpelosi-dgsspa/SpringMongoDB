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



    public Game fromStartGameDTOtoGameEntity(StartGameDTO startGameDTO){
        Game newGame = new Game();
        newGame.setGameLevel(startGameDTO.getLevel());
        newGame.setGameStatus(startGameDTO.getGameStatus());
        newGame.setLastupdateDate(startGameDTO.getStartDate());
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
