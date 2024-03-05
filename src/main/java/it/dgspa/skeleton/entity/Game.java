package it.dgspa.skeleton.entity;



import it.dgspa.skeleton.enumeration.GameLevel;
import it.dgspa.skeleton.enumeration.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.*;

@Document(collection = "game")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Game {


    @Id
    @Indexed(unique = true)
    private String idGame;

    private GameStatus gameStatus;

    private GameLevel gameLevel;

    private List<String> nicknamePlayers;

    private Integer totalScore;

    private Date lastupdateDate;





}











