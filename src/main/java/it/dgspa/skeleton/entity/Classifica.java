package it.dgspa.skeleton.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rank")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Classifica {


    @Id
    @Indexed(unique = true)
    private String idClassifica;

    private String  idGame;

    private String  idPlayer;

    private Integer punteggioTotale;

    private Boolean win;








}
