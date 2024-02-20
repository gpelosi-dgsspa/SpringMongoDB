package it.dgspa.skeleton.controller;

import it.dgspa.skeleton.dalImp.PlayerDALImpl;
import it.dgspa.skeleton.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/player")
public class PlayerController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PlayerDALImpl playerDAL;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Player> playerList(){
        log.info("Lista di tutti gli utenti");
        return playerDAL.playerList();
    }

    @RequestMapping(value = "/{playerId}", method = RequestMethod.GET)
    public Player getPlayer(@PathVariable String playerId){
        log.info("Estraggo Player con ID: {}.", playerId);
        return playerDAL.getPlayer(playerId);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Player addNewPlayer(@RequestBody Player player){
        log.info("Salvataggio utente");
        return playerDAL.addNewPlayer(player);
    }
}
