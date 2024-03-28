package it.dgspa.skeleton.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.dgspa.skeleton.GameLogic.PlayerStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "players")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Player{

    @MongoId
    @Indexed(unique = true)
    private String nickname;


    @Field
    private String email;


    @Field
    private String nome;


    @Field
    private String cognome;


    @Field
    private Integer eta;

    @Field
    private PlayerStatus statusGiocatore;

    @Field
    private Boolean win= false;

    @Field
    @JsonIgnore
    private Integer scorePlayer=0;


}


