package it.dgspa.skeleton.dalImp;

import it.dgspa.skeleton.Exception.PlayersException;
import it.dgspa.skeleton.GameLogic.PlayerStatus;
import it.dgspa.skeleton.dal.PlayerDAL;
import it.dgspa.skeleton.dto.PlayerDto;
import it.dgspa.skeleton.entity.Player;
import it.dgspa.skeleton.mapper.GamePlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerDALImpl implements PlayerDAL {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    GamePlayerMapper mapper;

    @Override
    public List<Player> playerList() {
        return mongoTemplate.findAll(Player.class);
    }

    @Override
    public Player getPlayer(String nickname) {
        Query query = new Query();
        query.addCriteria(Criteria.where("nickname").is(nickname));
        return mongoTemplate.findOne(query, Player.class);
    }

    @Override
    public Player addNewPlayer(PlayerDto player) {
        if (player.getEta() < 0) {
            throw new PlayersException("L'età del giocatore non può essere negativa.", player.getNickname());
        }
        Query query = new Query(Criteria.where("nickname").is(player.getNickname())
                .and("nome").is(player.getNome())
                .and("cognome").is(player.getCognome()));
        Player giocatoreEsistente = mongoTemplate.findOne(query, Player.class);
        if(giocatoreEsistente != null) {
            throw new PlayersException("Un giocatore con lo stesso nickname, nome e cognome esiste già nel database." ,player.getNickname());
        }
         Player p = mapper.fromPlayerDTOtoPlayerEntity(player);
         p.setStatusGiocatore(PlayerStatus.ATTIVO);
         p = mongoTemplate.save(p);
        return p;
    }


    }

