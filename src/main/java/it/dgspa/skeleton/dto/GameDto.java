package it.dgspa.skeleton.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class GameDto {

    @NotNull
    @NotBlank
    private Date dataPartita;

    //timestamp?????

    private List<GamePlayerDto> players;

}
