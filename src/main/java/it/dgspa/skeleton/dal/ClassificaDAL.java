package it.dgspa.skeleton.dal;
import it.dgspa.skeleton.dto.PlayerWinsDto;
import it.dgspa.skeleton.entity.Classifica;

public interface ClassificaDAL {

    public PlayerWinsDto getBestPlayer();
    public Classifica getBestScorePlayer();
    public int getFiveGamesBestScore(String idGiocatore);

    public Classifica createClassifica(Classifica c);




}
