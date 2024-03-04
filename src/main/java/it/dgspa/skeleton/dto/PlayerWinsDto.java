package it.dgspa.skeleton.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class PlayerWinsDto {

    @Field("idPlayer")
    private String  idPlayer;
    @Field("numVittorie")
    private Integer numVittorie;


}
