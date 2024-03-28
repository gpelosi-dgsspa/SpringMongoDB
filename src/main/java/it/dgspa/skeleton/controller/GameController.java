package it.dgspa.skeleton.controller;
import it.dgspa.skeleton.dalImp.GameDALImpl;
import it.dgspa.skeleton.dto.BestPlayerScoreDto;
import it.dgspa.skeleton.dto.BestScoreEverDto;
import it.dgspa.skeleton.dto.GameDto;
import it.dgspa.skeleton.entity.Game;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping(value = "/closeGame")
    public ResponseEntity<Object> closeGame(@RequestBody @Valid GameDto gameDto){
        return  gameDALImpl.closegame(gameDto);
    }



    @GetMapping(value ="/CountPlayed")
    public ResponseEntity<Integer> getCountAllGamesPlayedByPlayer (@RequestParam String nickname){

        return gameDALImpl.getCountAllGamesPlayedByPlayer(nickname);

    }


    @GetMapping(value="/BestPlayer")

    public ResponseEntity<String> getBestPlayer(@RequestParam String status){


        return gameDALImpl.getBestAbsoluteScorePlayer(status);
    }


    @GetMapping(value="/BestPlayerScoreFiveGames")

    public ResponseEntity<List<BestPlayerScoreDto>>getFive(){


        return gameDALImpl.getBestPlayerLast5Games();
    }

    @GetMapping(value ="/BestScoreEver")

    public ResponseEntity<List<BestScoreEverDto>> getBestScoreEver(){


        return gameDALImpl.getBestScoreEver();
    }

}
