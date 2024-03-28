package it.dgspa.skeleton.dto;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class GameDto {

    private Date dataPartita;
    private List<GamePlayerDto> players;

}
