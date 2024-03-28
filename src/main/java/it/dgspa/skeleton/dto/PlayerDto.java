package it.dgspa.skeleton.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerDto {

    private String nickname;

    @JsonIgnore
    private Boolean win;
}
