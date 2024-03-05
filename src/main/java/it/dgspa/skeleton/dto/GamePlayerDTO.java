package it.dgspa.skeleton.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GamePlayerDTO {

    @NotNull
    @NotBlank
    private String nickname;

    @NotNull
    private Integer score;

    private Boolean winner;
}
