package it.dgspa.skeleton.controller;


import it.dgspa.skeleton.dal.GameDAL;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value ="/games")
public class GameController {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private GameDAL gameDAL;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Game> gameList(){
        log.info("Lista di tutte le partite");
        return gameDAL.getAllGames();
    }



    @RequestMapping(value = "/createGame", method = RequestMethod.POST)
    public Game addNewGame(@RequestBody Game game){
        log.info("Salvataggio della partita");
        return gameDAL.addNewGame(game);
    }

   @RequestMapping(value ="/PlayedGames",method = RequestMethod.GET)
    public List<Game> getAllGamesPlayedByPlayer (String id){

       return gameDAL.getAllGamesPlayedByPlayer(id);

   }

    @RequestMapping(value ="/WinGames",method = RequestMethod.GET)
    public List<Game> getAllWinGames (Player p){

        return gameDAL.getAllWinGames(p);

    }

    @RequestMapping(value ="/CountLooseGames",method = RequestMethod.GET)
    public Integer getCountAllGamesLoose (Player p){

        return gameDAL.getAllGamesLooseByPlayer(p);

    }

    @RequestMapping(value ="/CountGamesPlayed",method = RequestMethod.GET)
    public Integer getCountAllGamesPlayed (String id){

        return gameDAL.getCountAllGamesPlayedByPlayer(id);

    }
    @RequestMapping(value ="/CountWinGames",method = RequestMethod.GET)
    public Integer getCountAllGamesWin (String id){

        return gameDAL.getAllGamesWinByPlayer(id);

    }




}
