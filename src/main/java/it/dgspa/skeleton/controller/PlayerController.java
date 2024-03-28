package it.dgspa.skeleton.controller;
import it.dgspa.skeleton.dalImp.PlayerDALImpl;
import it.dgspa.skeleton.dto.PlayerInsertDto;
import it.dgspa.skeleton.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/{nickname}", method = RequestMethod.GET)
    public ResponseEntity<Object> getPlayer(@PathVariable String nickname){
        log.info("Estraggo Player con ID: {}.", nickname);
        return playerDAL.getPlayer(nickname);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewPlayer(@RequestBody PlayerInsertDto player){
        log.info("Salvataggio utente");
        return playerDAL.addNewPlayer(player);
    }
}
