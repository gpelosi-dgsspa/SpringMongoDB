package it.dgspa.skeleton.dto;

import it.dgspa.skeleton.enumeration.GameLevel;
import it.dgspa.skeleton.enumeration.GameStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GameDTO {

    private String idGame;

    private GameLevel level;

    private List<String> gamePlayers;

    private GameStatus gameStatus;

    private Date startDate;

    private Date endDate;

}
