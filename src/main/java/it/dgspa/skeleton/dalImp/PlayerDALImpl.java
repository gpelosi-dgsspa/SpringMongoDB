package it.dgspa.skeleton.dalImp;

import it.dgspa.skeleton.dal.PlayerDAL;
import it.dgspa.skeleton.entity.Player;
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

    @Override
    public List<Player> playerList() {
        return mongoTemplate.findAll(Player.class);
    }

    @Override
    public Player getPlayer(String playerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(playerId));
        /*return mongoTemplate.findByI*/
        return mongoTemplate.findById(query, Player.class);
    }

    @Override
    public Player addNewPlayer(Player player) {
        mongoTemplate.save(player);
        return player;
    }
}
