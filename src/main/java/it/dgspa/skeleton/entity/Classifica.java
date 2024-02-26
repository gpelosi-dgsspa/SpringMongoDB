package it.dgspa.skeleton.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Classifica {


    @Id
    private String idClassifica;

    private String idGame;

    private String  idPlayer;

    private Integer punteggioTotale;

    private Boolean win;








}
