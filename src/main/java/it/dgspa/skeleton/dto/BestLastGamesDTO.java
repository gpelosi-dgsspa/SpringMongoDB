package it.dgspa.skeleton.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BestLastGamesDTO {

    private Integer index;
    private String gameId;
    private Date gameDate;
    private String player;
    private Integer score;
    private Boolean winner;
}
