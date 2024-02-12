package it.dgspa.skeleton.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.ZonedDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Player {

    @Id
    private Integer idPlayer;

    @Field
    private String nome;
    @Field
    private String cognome;
    @Field
    private Integer eta;
    @Field
   private String nickname;
    @Field
    private Integer livelloGiocatore;



}
