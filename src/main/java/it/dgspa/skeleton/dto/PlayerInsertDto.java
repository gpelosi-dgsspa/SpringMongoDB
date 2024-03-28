package it.dgspa.skeleton.dto;

import lombok.Data;

@Data
public class PlayerInsertDto {


    private String nickname;

    private String email;

    private Integer eta;

    private String nome;

    private String cognome;
}
