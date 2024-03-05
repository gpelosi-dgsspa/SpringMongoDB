package it.dgspa.skeleton.entity;

import it.dgspa.skeleton.enumeration.PlayerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "player")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Player{

    @Id
    @Indexed(unique = true)
    private String nickname;

    private Integer eta;

    private PlayerStatus playerStatus;



}


