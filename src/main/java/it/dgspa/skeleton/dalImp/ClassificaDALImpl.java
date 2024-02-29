package it.dgspa.skeleton.dalImp;

import it.dgspa.skeleton.dal.ClassificaDAL;
import it.dgspa.skeleton.dto.PlayerWinsDto;
import it.dgspa.skeleton.entity.Classifica;
import it.dgspa.skeleton.entity.Player;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassificaDALImpl implements ClassificaDAL {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    GameDALImpl gameImpl;

    @Autowired
    PlayerDALImpl playerImpl;




/*

  quante partite ha giocato un giocatore

  quante partite e quali partite ha vinto o perso un giocatore

  CHI è IL GIOCATORE CHE HA IL BEST SCORES IN ASSOLUTO, E CON QUALE SCORE
  CHI è IL GIOCATORE CHE HA VINTO PIù PARTITE E QUANTE SONO
  MIGLIOR PUNTEGGIO-GIOCATORE NELLE ULTIME 5 PARTITE


 */
  public Classifica createClassifica(Classifica c){

      mongoTemplate.save(c);
      return c;
  }

    public List<Classifica> getAllRank(){
        return mongoTemplate.findAll(Classifica.class);
    }

    public Classifica getBestScorePlayer(){


        Query query = new Query()
                .limit(1)
                .with(Sort.by(Sort.Direction.DESC, "punteggioTotale"));

        return mongoTemplate.findOne(query, Classifica.class);


    }

      public PlayerWinsDto getBestPlayer(){

          GroupOperation groupByPlayerId = Aggregation.group("idPlayer").count().as("count");
          MatchOperation matchWonGames = Aggregation.match(Criteria.where("win").is(true));
          Aggregation sortAndLimit = Aggregation.newAggregation(matchWonGames, groupByPlayerId,
                  Aggregation.sort(Sort.by(Sort.Direction.DESC, "count")),
                  Aggregation.limit(1));

          AggregationResults<PlayerWinsDto> results = mongoTemplate.aggregate(sortAndLimit, "rank", PlayerWinsDto.class);
          return results.getUniqueMappedResult();
      }





    public int getFiveGamesBestScore(String idGiocatore) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("listaPartecipanti.idGiocatore").is(idGiocatore)),
                Aggregation.sort(Sort.Direction.DESC, "dataPartita"),
                Aggregation.limit(5),
                Aggregation.unwind("listaPartecipanti"),
                Aggregation.match(Criteria.where("listaPartecipanti.idGiocatore").is(idGiocatore)),
                Aggregation.group().max("listaPartecipanti.punteggio").as("migliorPunteggio")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "games", Document.class);
        Document bestScoreResult = results.getUniqueMappedResult();

        if (bestScoreResult != null && bestScoreResult.containsKey("migliorPunteggio")) {
            Object bestScoreObject = bestScoreResult.get("migliorPunteggio");
            if (bestScoreObject instanceof Number) {
                return ((Number) bestScoreObject).intValue();
            }
        }
        return 0;
    }




    }











