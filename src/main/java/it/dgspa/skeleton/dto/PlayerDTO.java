package it.dgspa.skeleton.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.dgspa.skeleton.enumeration.PlayerStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlayerDTO {

    @NotNull
    @NotBlank
    private String nickname;

    @NotNull
    @NotBlank
    @Min(value = 14, message = "From must be greater than 14")
    private Integer eta;

    @JsonIgnore
    private PlayerStatus playerStatus;
}
