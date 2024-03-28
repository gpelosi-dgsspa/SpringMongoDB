package it.dgspa.skeleton.mapper;


import it.dgspa.skeleton.dto.GameDto;
import it.dgspa.skeleton.dto.GamePlayerDto;
import it.dgspa.skeleton.dto.PlayerDto;
import it.dgspa.skeleton.dto.PlayerInsertDto;
import it.dgspa.skeleton.entity.Player;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GamePlayerMapper {

    public Player fromPlayerDTOtoPlayerEntity(PlayerDto playerDTO){
        Player newPlayer = new Player();
        newPlayer.setNickname(playerDTO.getNickname());
        return newPlayer;
    }



    public Player fromPlayerInsertDTOtoPlayerEntity(PlayerInsertDto playerDTO){
        Player newPlayer = new Player();
        newPlayer.setEta(playerDTO.getEta());
        newPlayer.setNickname(playerDTO.getNickname());
        newPlayer.setEmail(playerDTO.getEmail());
        newPlayer.setNome(playerDTO.getNome());
        newPlayer.setCognome(playerDTO.getCognome());
        return newPlayer;
    }




}
