package it.dgspa.skeleton.dto;

import it.dgspa.skeleton.GameLogic.PlayerGameLevel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class GamePlayerDto {



    @Valid
    @NotNull
    @Positive
    @Min(0)
    @Max(1000)
    private Integer score;

    private PlayerDto player;



}
