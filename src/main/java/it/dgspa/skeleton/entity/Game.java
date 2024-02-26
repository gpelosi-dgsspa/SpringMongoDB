package it.dgspa.skeleton.entity;



import it.dgspa.skeleton.GameLogic.GameEnum;
import it.dgspa.skeleton.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.ZonedDateTime;
import java.util.*;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class Game {


    @Id
    private String idGame;
    private GameEnum statoGioco;

    private List<Player> listaPartecipanti;

    private ZonedDateTime dataPartita;



    private String idVincitore;




}











