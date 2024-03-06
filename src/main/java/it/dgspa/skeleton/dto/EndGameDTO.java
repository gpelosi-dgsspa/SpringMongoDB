package it.dgspa.skeleton.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.dgspa.skeleton.enumeration.GameStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EndGameDTO {

    private String gameId;

    @NotNull
    private List<GamePlayerDTO> gamePlayers;

    @JsonIgnore
    private Date endDate;

    @JsonIgnore
    private GameStatus gameStatus;


}
