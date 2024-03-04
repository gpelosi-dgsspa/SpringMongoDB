package it.dgspa.skeleton.controller;

import it.dgspa.skeleton.dalImp.ClassificaDALImpl;
import it.dgspa.skeleton.dto.PlayerWinsDto;
import it.dgspa.skeleton.entity.Classifica;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value ="/rank")
public class ClassificaController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ClassificaDALImpl classImpl;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Classifica> RankList(){
        log.info("classifica generale");
        return classImpl.getAllRank();
    }

    @GetMapping(value ="/FiveGames")
    public Object getFiveGamesBestScore(@PathParam("idGiocatore") String idGiocatore){
        log.info("il miglior Punteggio nelle ultime cinque partite");
        return classImpl.getFiveGamesBestScore(idGiocatore);
    }
    @RequestMapping(value ="/BestScorePlayer",method = RequestMethod.GET)
    public Classifica getBestScorePlayer(){
        return classImpl.getBestScorePlayer();
    }
    @RequestMapping(value ="/BestPlayer",method = RequestMethod.GET)
    public PlayerWinsDto getBestPlayer(){
        return classImpl.getBestPlayer();
    }



    @PostMapping(value= "/createClassifica")
    public Classifica createClassifica(@RequestBody Classifica c){

       return  classImpl.createClassifica(c);
    }


}
