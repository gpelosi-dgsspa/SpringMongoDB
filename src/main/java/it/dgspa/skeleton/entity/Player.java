package it.dgspa.skeleton.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.dgspa.skeleton.GameLogic.PlayerGameLevel;
import it.dgspa.skeleton.GameLogic.PlayerStatus;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "players")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Player{

    @Id
    @Indexed(unique = true)
    private String nickname;


    @Field
    private String nome;


    @Field
    private String cognome;


    @Field
    private Integer eta;
    @Field
    private PlayerGameLevel livelloGiocatore;

    @Field
    private PlayerStatus statusGiocatore;

    @Field
    private Boolean win= false;

    @Field
    @JsonIgnore
    private Integer scorePlayer=0;


}


