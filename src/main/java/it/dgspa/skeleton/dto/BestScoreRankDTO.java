package it.dgspa.skeleton.dto;

import lombok.Data;

@Data
public class BestScoreRankDTO {
    private Integer index;
    private String player;
    private Integer bestScore;
}
