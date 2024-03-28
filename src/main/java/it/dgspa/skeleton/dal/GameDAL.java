package it.dgspa.skeleton.dal;
import it.dgspa.skeleton.dto.BestPlayerScoreDto;
import it.dgspa.skeleton.dto.BestScoreEverDto;
import it.dgspa.skeleton.dto.GameDto;
import it.dgspa.skeleton.entity.Game;
import org.springframework.http.ResponseEntity;
import java.util.Date;
import java.util.List;

public interface GameDAL {

    List<Game>  getAllGames();

    public   ResponseEntity<Object> closegame (GameDto gameDto);

    public ResponseEntity<Integer> getCountAllGamesPlayedByPlayer(String  nickname);

    public ResponseEntity<List<BestScoreEverDto>> getBestScoreEver();
    public ResponseEntity<List<BestPlayerScoreDto>> getBestPlayerLast5Games();


}