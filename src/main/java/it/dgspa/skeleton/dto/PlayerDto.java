package it.dgspa.skeleton.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.dgspa.skeleton.GameLogic.PlayerGameLevel;
import it.dgspa.skeleton.GameLogic.PlayerStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
public class PlayerDto {
    @javax.validation.constraints.NotNull
    @javax.validation.constraints.NotBlank
    private String nickname;

    @javax.validation.constraints.NotBlank
    @javax.validation.constraints.NotNull
    private String nome;

    @javax.validation.constraints.NotBlank
    @javax.validation.constraints.NotNull
    private String cognome;
    @javax.validation.constraints.NotNull
    @javax.validation.constraints.Min(14)
    @javax.validation.constraints.Max(100)
    private Integer eta;
    private PlayerGameLevel livelloGiocatore;

    @JsonIgnore
    private Boolean win;
}
