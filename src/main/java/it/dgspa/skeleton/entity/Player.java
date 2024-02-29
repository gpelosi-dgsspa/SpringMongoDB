package it.dgspa.skeleton.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "players")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Player{

    @Id
    @Indexed(unique = true)
    private String id;

    private String idGiocatore;

    private String nome;

    private String cognome;

    private Integer eta;

   private String nickname;

    private Integer livelloGiocatore;

    private Integer punteggio;

}


