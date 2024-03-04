package it.dgspa.skeleton.dalImp;
import it.dgspa.skeleton.dal.GameDAL;
import it.dgspa.skeleton.entity.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameDALImpl implements GameDAL {

    @Autowired
    MongoTemplate mongoTemplate;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public List<Game> getAllGames() {
        Query query = new Query();
        return mongoTemplate.find(query,Game.class);
    }


    public List<Game> findGamesById(String idplayer) {
        Query query = new Query(Criteria.where("id").is(idplayer));

        return mongoTemplate.find(query, Game.class);
    }



    public Game addNewGame(Game g) {

        mongoTemplate.save(g);

        return g;
    }

    // quali partite

    public List<Game> getAllGamesPlayedByPlayer(String id) {


        TypedAggregation<Game> aggregation = Aggregation.newAggregation(Game.class,
                Aggregation.match(Criteria.where("listaPartecipanti.idGiocatore").is(id)));


        System.out.println(aggregation.toString());
        List<Game> results = mongoTemplate.aggregate(aggregation, Game.class).getMappedResults();

        return results;

    }
    //quante partite

    public Integer getCountAllGamesPlayedByPlayer(String idGiocatore) {

        return this.getAllGamesPlayedByPlayer(idGiocatore).size();
    }


    // numero partite vinte
    public Integer getAllGamesWinByPlayer(String idGiocatore1) {

        return this.getAllGamesPlayedByPlayer(idGiocatore1).
                stream().
                filter(g -> g.getIdVincitore().equals(idGiocatore1)).
                collect(Collectors.toList()).size();


    }

    //numero partite perse

    public Integer getAllGamesLooseByPlayer(String idGiocatore) {

        Query query = new Query();
        query.addCriteria(Criteria.where("listaPartecipanti").elemMatch(Criteria.where("idGiocatore").is(idGiocatore)).and("idVincitore").ne(idGiocatore));
        return mongoTemplate.find(query, Game.class).size();
    }

// quali vinte
    public List<Game> getAllWinGames(String idGiocatore) {

        return this.getAllGamesPlayedByPlayer(idGiocatore).
                stream().
                filter(g -> g.getIdVincitore().equals (idGiocatore)).
                collect(Collectors.toList());

    }

  //quali perse
    public List<Game> getAllGamesLoose(String   idGiocatore) {

        Query query = new Query();
        query.addCriteria(Criteria.where("listaPartecipanti").elemMatch(Criteria.where("idGiocatore").is(idGiocatore).and("idVincitore").ne(idGiocatore)));
        return mongoTemplate.find(query, Game.class);

    }

}





