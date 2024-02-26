package it.dgspa.skeleton.dal;

import it.dgspa.skeleton.dto.PlayerWinsDto;
import it.dgspa.skeleton.entity.Classifica;

import java.util.List;

public interface ClassificaDAL {

    public PlayerWinsDto getBestPlayer();
    public Classifica getBestScorePlayer();




}
