package it.dgspa.skeleton.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.dgspa.skeleton.enumeration.GameLevel;
import it.dgspa.skeleton.enumeration.GameStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StartGameDTO {


    @NotNull
    private GameLevel level;

    @NotNull
    private List<String> gamePlayers;

    @JsonIgnore
    private Date startDate;

    @JsonIgnore
    private GameStatus gameStatus;

}
