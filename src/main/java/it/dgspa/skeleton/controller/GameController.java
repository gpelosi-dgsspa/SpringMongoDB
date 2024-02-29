package it.dgspa.skeleton.controller;


;
import it.dgspa.skeleton.dalImp.GameDALImpl;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value="/Games")
public class GameController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    GameDALImpl gameDALImpl;

    @RequestMapping(value = "/All", method = RequestMethod.GET)
    public List<Game> gameList(){
        log.info("Lista di tutte le partite");
        return gameDALImpl.getAllGames();
    }

    @PostMapping(value = "/createGame")
    public Game createGame(@RequestBody Game g){
        return gameDALImpl.addNewGame(g);
    }

   @GetMapping(value="/AllGamesPlayed")
    public
   List<Game> getAllGamesPlayedByPlayer (@RequestParam String id){
       log.info("ciao, siamo qui");
       return gameDALImpl.getAllGamesPlayedByPlayer(id);

   }

    @RequestMapping(value ="/WinGames",method = RequestMethod.GET)
    public List<Game> getAllWinGames (@RequestParam String  idGiocatore){

        return gameDALImpl.getAllWinGames(idGiocatore);

    }

    @RequestMapping(value ="/CountLooseGames",method = RequestMethod.GET)
    public Integer getCountAllGamesLoose (@RequestParam String  idGiocatore){

        return gameDALImpl.getAllGamesLooseByPlayer(idGiocatore);

    }

    @RequestMapping(value ="/CountAllGames",method = RequestMethod.GET)
    public Integer getCountAllGamesPlayed (@RequestParam String  idGiocatore){

        return gameDALImpl.getCountAllGamesPlayedByPlayer(idGiocatore);

    }
    @RequestMapping(value ="/CountWinGames",method = RequestMethod.GET)
    public Integer getCountAllGamesWin (@RequestParam String  idGiocatore){

        return gameDALImpl.getAllGamesWinByPlayer(idGiocatore);

    }



}
