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

    public ResponseEntity<Object> addOrUpdatePlayer(PlayerDTO playerDTO, boolean insert){
        Query query = new Query();
        query.addCriteria(Criteria.where("nickname").is(playerDTO.getNickname()));
        List<Player> foundedPlayers = mongoTemplate.find(query,Player.class);
        if(insert && foundedPlayers!=null && foundedPlayers.size()>0){
            return new ResponseEntity<>("User "+playerDTO.getNickname()+" already exist.", HttpStatus.OK);
        }
        if(!insert && (foundedPlayers==null || foundedPlayers.size()==0)){
            return new ResponseEntity<>("User "+playerDTO.getNickname()+" doesn't exist.", HttpStatus.OK);
        }
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
            Query query = new Query();
            query.addCriteria(Criteria.where("idGame").is(endGameDTO.getGameId()));
            List<Game> foundedGameList = mongoTemplate.find(query,Game.class);
            if(foundedGameList!=null && foundedGameList.size()==1){
                Game game = foundedGameList.get(0);
                if(game.getGameStatus().equals(GameStatus.TERMINATA)){
                    return new ResponseEntity<>("Game already terminated", HttpStatus.BAD_REQUEST);
                }
                game.setGameStatus(endGameDTO.getGameStatus());
                int totalScore = 0;
                for(GamePlayerDTO gamePlayer: endGameDTO.getGamePlayers()){
                totalScore += gamePlayer.getScore();
                Ranking rank = new Ranking();
                rank.setIdGame(game.getIdGame());
                rank.setIdPlayer(gamePlayer.getNickname());
                rank.setWinner(gamePlayer.getWinner());
                rank.setGameScore(gamePlayer.getScore());
                mongoTemplate.save(rank);
                }
                game.setTotalScore(totalScore);
                game.setLastupdateDate(endGameDTO.getEndDate());
                game = mongoTemplate.save(game);
                return new ResponseEntity<>(game, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Game not exist", HttpStatus.BAD_REQUEST);
            }


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
        gameQuery.limit(5);
        List<Game>  lastFiveGames = mongoTemplate.find(gameQuery, Game.class);
        int index = 1;
        for(Game game:lastFiveGames){
            BestLastGamesDTO bestLastGamesDTO = new BestLastGamesDTO();
            bestLastGamesDTO.setIndex(index);
            bestLastGamesDTO.setGameId(game.getIdGame());
            bestLastGamesDTO.setGameDate(game.getLastupdateDate());

            Query ratingQuery = new Query();
            ratingQuery.addCriteria(Criteria.where("idGame").is(game.getIdGame()));
            ratingQuery.with(Sort.by(Sort.Direction.DESC,"gameScore"));
            ratingQuery.limit(1);
            Ranking rank = mongoTemplate.find(ratingQuery, Ranking.class).get(0);

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
                group("idPlayer").count().as("totalWins"),
                sort(Sort.by(Sort.Direction.DESC, "totalWins")),
                limit(3)
        );

        List<AggregateRanking> aggregateRankings = mongoTemplate.aggregate(aggregation, AggregateRanking.class).getMappedResults();
        int index = 1;
        for (AggregateRanking ar : aggregateRankings) {
            BestWinRankDTO bestWinRankDTO = new BestWinRankDTO();
            bestWinRankDTO.setIndex(index);
            bestWinRankDTO.setPlayer(ar.getIdPlayer());
            bestWinRankDTO.setWins(ar.getTotalWins());
            bestWinRankDTOList.add(bestWinRankDTO);
            index++;
        }

        return new ResponseEntity<>(bestWinRankDTOList,HttpStatus.OK);
    }

    public ResponseEntity<List<BestScoreRankDTO>> getBestScoreRank(){
        List<BestScoreRankDTO> bestScoreRankDTOList = new ArrayList<>();

        Query query = new Query();
        query.with(Sort.by(Sort.Direction.DESC,"gameScore"));
        query.limit(3);
        List<Ranking> rankings= mongoTemplate.find(query,Ranking.class);
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


    public ResponseEntity<String> dropAllData(){
        mongoTemplate.remove( new Query(), Ranking.class);
        mongoTemplate.remove(new Query(), Game.class);
        mongoTemplate.remove(new Query(), Player.class);
        return new ResponseEntity<>("Database succesfully erased",HttpStatus.OK);
    }
}
