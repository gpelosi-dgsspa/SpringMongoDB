package it.dgspa.skeleton.controller;


;
import it.dgspa.skeleton.dalImp.GameDALImpl;
import it.dgspa.skeleton.dto.BestPlayerScoreDto;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;
import jakarta.websocket.server.PathParam;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Object> createGame(@RequestBody Boolean insert){
        return  gameDALImpl.startNewGame(insert);
    }

    @PostMapping(value = "/closeGame")
    public ResponseEntity<Object> closeGame(@RequestBody String idGame){
        return  gameDALImpl.closegame(idGame);
    }


    @GetMapping(value ="/AllGamesPlayed")
    public ResponseEntity<List<Game>> getAllGamesPlayedByPlayer(@RequestParam String nickname){

        return gameDALImpl.getAllGamesPlayedByPlayer(nickname);
    }

    @GetMapping(value ="/CountPlayed")
    public ResponseEntity<Integer> getCountAllGamesPlayedByPlayer (@RequestParam String nickname){

        return gameDALImpl.getCountAllGamesPlayedByPlayer(nickname);

    }

    @GetMapping(value ="/CountWinGames")

    public ResponseEntity<Integer> getCountAllWinGames (@RequestParam String nickname){

        return gameDALImpl.countAllGamesPlayerWins(nickname);
    }

    @GetMapping (value ="/CountLooseGames")
    public ResponseEntity<Integer> getAllLooseGames (@RequestParam String nickname){

        return gameDALImpl.countAllGamesPlayerLosses(nickname);
    }

    @GetMapping(value ="/AllLostGames")
    public ResponseEntity<List<Game>> getAllLostGames(@RequestParam String nickname){

        return gameDALImpl.getAllLostGames(nickname);
    }

    @GetMapping(value ="/AllWinGames")
    public ResponseEntity<List<Game>> getAllWinGames(@RequestParam String nickname){

        return gameDALImpl.getAllWinGames(nickname);
    }
    @GetMapping(value="/BestPlayer")

    public ResponseEntity<String> getBestPlayer(@RequestParam String status){


        return gameDALImpl.getBestAbsoluteScorePlayer(status);
    }

    @GetMapping(value="/CountPlusWingGames")

    public ResponseEntity<Integer> getCountPlusWinGames(@RequestParam String status){


        return gameDALImpl.getCountPlusWingGames(status);
    }

    @GetMapping(value="/BestPlayerScoreFiveGames")

    public ResponseEntity<Map<BestPlayerScoreDto,Integer>>getFive(){


        return gameDALImpl.getBestPlayerLast5Games();
    }

}
