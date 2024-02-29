package it.dgspa.skeleton.entity;



import com.fasterxml.jackson.annotation.JsonProperty;
import it.dgspa.skeleton.GameLogic.GameEnum;
import it.dgspa.skeleton.entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import java.time.ZonedDateTime;
import java.util.*;

@Document(collection = "games")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Game {


    @Id
    @Indexed(unique = true)
    private String idGame;
    private GameEnum statoGioco;

    private  List<Player> listaPartecipanti;

    private Date dataPartita;
    private String  idVincitore;




}











