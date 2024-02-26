package it.dgspa.skeleton.dalImp;
import it.dgspa.skeleton.dal.GameDAL;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameDALImpl implements GameDAL {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Game> getAllGames() {
        return mongoTemplate.findAll(Game.class);
    }

    @Override
    public List<Game> findGamesById(String idplayer) {
        Query query = new Query(Criteria.where("id").is(idplayer));

        return mongoTemplate.find(query, Game.class);
    }


    @Override
    public Game addNewGame(Game g) {

        mongoTemplate.save(g);

        return g;
    }

    // quali partite
    @Override
    public List<Game> getAllGamesPlayedByPlayer(String idPlayer) {

        AggregationOperation matchOperation = Aggregation.match(Criteria.where("listaPartecipanti.idGiocatore").is(idPlayer));

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.unwind("listaPartecipanti"),
                matchOperation
        );

        return mongoTemplate.aggregate(aggregation, "game", Game.class).getMappedResults();
    }




    //quante partite

    public Integer getCountAllGamesPlayedByPlayer(String idPlayer) {

        return this.getAllGamesPlayedByPlayer(idPlayer).size();
    }


    // numero partite vinte
    public Integer getAllGamesWinByPlayer(String idPlayer) {

        return this.getAllGamesPlayedByPlayer(idPlayer).
                stream().
                filter(g -> g.getIdVincitore().equals(idPlayer)).
                collect(Collectors.toList()).size();


    }

    //numero partite perse

    public Integer getAllGamesLooseByPlayer(Player p) {

        Query query = new Query();
        query.addCriteria(Criteria.where("listaPartecipanti").elemMatch(Criteria.where("id").is(p.getId())).and("idVincitore").ne(p.getId()));
        return mongoTemplate.find(query, Game.class).size();
    }

    /*

    quali partite vinte  e perse


     */
    public List<Game> getAllWinGames(Player p) {

        return this.getAllGamesPlayedByPlayer(p.getId()).
                stream().
                filter(g -> g.getIdVincitore().equals(p.getId())).
                collect(Collectors.toList());

    }

}





