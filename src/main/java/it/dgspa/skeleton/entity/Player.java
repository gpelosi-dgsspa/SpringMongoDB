package it.dgspa.skeleton.entity;
import it.dgspa.skeleton.GameLogic.PlayerGameLevel;
import it.dgspa.skeleton.GameLogic.PlayerStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
public class Player{

    @Id
    @Indexed(unique = true)
    @NotNull
    @NotBlank
    private String nickname;


    @Field
    @NotNull
    @NotBlank
    private String nome;


    @Field
    @NotNull
    @NotBlank
    private String cognome;


    @Field
    @NotNull
    @Positive
    @Min(18)
    @Max(100)
    private Integer eta;
    @Field
    @NotBlank
    @NotNull
    private PlayerGameLevel livelloGiocatore;

    @Field
    @NotBlank
    @NotNull
    private PlayerStatus statusGiocatore;

    @Field
    private Boolean win= false;

    @Field
    @NotNull
    private Integer scorePlayer=0;


}


