package it.dgspa.skeleton.service;

import it.dgspa.skeleton.enumeration.GameStatus;
import it.dgspa.skeleton.dto.*;
import it.dgspa.skeleton.entity.AggregateRanking;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;
import it.dgspa.skeleton.entity.Ranking;
import it.dgspa.skeleton.mapper.GamePlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class GamePlayerService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GamePlayerMapper mapper;

    public ResponseEntity<Player> addOrUpdatePlayer(PlayerDTO playerDTO, boolean insert){
        //TO-DO verificare che il nickname non esiste già in caso di nuovo inserimento
        Player newPlayer = mapper.fromPlayerDTOtoPlayerEntity(playerDTO);
        newPlayer = mongoTemplate.save(newPlayer);
        return new ResponseEntity<>(newPlayer, HttpStatus.OK);
    }

    public ResponseEntity<Game> startGame(StartGameDTO startGameDTO){
        Game newGame = mapper.fromStartGameDTOtoGameEntity(startGameDTO);
        newGame = mongoTemplate.save(newGame);
        return new ResponseEntity<>(newGame, HttpStatus.OK);
    }

    public ResponseEntity<Object> closeGame(EndGameDTO endGameDTO){
        try {
            Game updateGame = mapper.fromEndGameDTOtoGameEntity(endGameDTO);
            updateGame = mongoTemplate.save(updateGame);
            return new ResponseEntity<>(updateGame, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Player>> allPlayers(){
        return new ResponseEntity<>(mongoTemplate.findAll(Player.class), HttpStatus.OK);

    }

    public ResponseEntity<List<Game>> allGames(){
        return new ResponseEntity<>(mongoTemplate.findAll(Game.class), HttpStatus.OK);

    }

    public ResponseEntity<List<BestLastGamesDTO>> getBestScoreLast5Games(){
        List<BestLastGamesDTO> bestLastGamesDTOList = new ArrayList<>();
        Query gameQuery = new Query();
        gameQuery.addCriteria(Criteria.where("gameStatus").is(GameStatus.TERMINATA));
        gameQuery.with(Sort.by(Sort.Direction.DESC,"lastupdateDate"));
        List<Game>  lastFiveGames = mongoTemplate.find(gameQuery, Game.class);
        int index = 1;
        for(Game game:lastFiveGames){
            BestLastGamesDTO bestLastGamesDTO = new BestLastGamesDTO();
            bestLastGamesDTO.setGameId(game.getIdGame());
            bestLastGamesDTO.setGameDate(game.getLastupdateDate());
            Query ratingQuery = new Query();
            ratingQuery.addCriteria(Criteria.where("idGame").is(game.getIdGame()));
            ratingQuery.with(Sort.by(Sort.Direction.DESC,"gameScore"));
            ratingQuery.limit(1);
            Ranking rank = mongoTemplate.find(ratingQuery, Ranking.class).get(0);
            bestLastGamesDTO.setIndex(index);
            bestLastGamesDTO.setPlayer(rank.getIdPlayer());
            bestLastGamesDTO.setScore(rank.getGameScore());
            bestLastGamesDTO.setWinner(rank.getWinner());
            bestLastGamesDTOList.add(bestLastGamesDTO);
            index++;
        }
        return new ResponseEntity<>(bestLastGamesDTOList,HttpStatus.OK);
    }

    public ResponseEntity<List<BestWinRankDTO>> getBestWinnerRank(){
        List<BestWinRankDTO> bestWinRankDTOList = new ArrayList<>();

        TypedAggregation<Ranking> aggregation = Aggregation.newAggregation(Ranking.class,
                match(Criteria.where("winner").is(true)),
                group("idPlayer").count().as("total"),
                sort(Sort.by(Sort.Direction.DESC,"total")),
                limit(3)
        );
        List<AggregateRanking> aggregateRankings= mongoTemplate.aggregate(aggregation, AggregateRanking.class).getMappedResults();
        int index =1;
        for(AggregateRanking ar: aggregateRankings){

            BestWinRankDTO bestWinRankDTO = new BestWinRankDTO();
            bestWinRankDTO.setIndex(index);
            bestWinRankDTO.setPlayer(ar.getIdPlayer());
            bestWinRankDTO.setWins(ar.getTotal());
            bestWinRankDTOList.add(bestWinRankDTO);

            index++;

        }

        return new ResponseEntity<>(bestWinRankDTOList,HttpStatus.OK);
    }

    public ResponseEntity<List<BestScoreRankDTO>> getBestScoreRank(){
        List<BestScoreRankDTO> bestScoreRankDTOList = new ArrayList<>();

        TypedAggregation<Ranking> aggregation = Aggregation.newAggregation(Ranking.class,
                group("idPlayer","gameScore"),
                sort(Sort.by(Sort.Direction.DESC,"gameScore")),
                limit(3)
        );
        List<Ranking> rankings= mongoTemplate.aggregate(aggregation, Ranking.class).getMappedResults();
        int index =1;
        for(Ranking rank: rankings){

            BestScoreRankDTO bestScoreRankDTO = new BestScoreRankDTO();
            bestScoreRankDTO.setIndex(index);
            bestScoreRankDTO.setPlayer(rank.getIdPlayer());
            bestScoreRankDTO.setBestScore(rank.getGameScore());
            bestScoreRankDTOList.add(bestScoreRankDTO);

            index++;

        }

        return new ResponseEntity<>(bestScoreRankDTOList,HttpStatus.OK);
    }


}
