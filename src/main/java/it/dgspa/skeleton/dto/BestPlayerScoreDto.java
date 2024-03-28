package it.dgspa.skeleton.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BestPlayerScoreDto {


    private Integer position;
    private String nickname;

    private Integer score;
}
