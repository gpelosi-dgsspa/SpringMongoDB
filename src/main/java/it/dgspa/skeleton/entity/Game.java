package it.dgspa.skeleton.entity;
import it.dgspa.skeleton.GameLogic.GameEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.*;

@Document(collection = "games")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Game {

    @MongoId
    @Indexed(unique = true)
    private Date dataPartita;


    @Field
    private GameEnum statoGioco;

    @Field
    private  List<Player> listaPartecipanti;

    @Field
    private Date dataFinePartita;
    @Field
    //classifica relativa di fine partita, giocatore-punteggio
    private Map<String,Integer> classifica;






}











