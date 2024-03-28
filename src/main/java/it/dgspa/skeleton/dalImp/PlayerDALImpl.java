package it.dgspa.skeleton.dalImp;


import it.dgspa.skeleton.GameLogic.PlayerStatus;
import it.dgspa.skeleton.dal.PlayerDAL;
import it.dgspa.skeleton.dto.PlayerInsertDto;
import it.dgspa.skeleton.entity.Player;
import it.dgspa.skeleton.mapper.GamePlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class PlayerDALImpl implements PlayerDAL {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    GamePlayerMapper mapper;

    @Override
    public List<Player> playerList() {
        return mongoTemplate.findAll(Player.class);
    }

    @Override
    public ResponseEntity<Object> getPlayer(String nickname) {
        Query q = new Query(Criteria.where("nickname").is(nickname));
        Player p = mongoTemplate.findOne(q,Player.class);
        if (p == null) {
            return new ResponseEntity<>("Il nickname non è presente nella base dati", HttpStatus.NOT_FOUND);
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("nickname").is(nickname));
        return  new ResponseEntity<>(mongoTemplate.findOne(query, Player.class),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> addNewPlayer(PlayerInsertDto player) {
        String nickname = player.getNickname();
        if (nickname == null || nickname.isEmpty()) {
            return new ResponseEntity<>("Il campo nickname non può essere vuoto", HttpStatus.BAD_REQUEST);
        }
        String nome = player.getNome();
        if (nome == null || nome.isEmpty()) {
            return new ResponseEntity<>("Il campo nome non può essere vuoto", HttpStatus.BAD_REQUEST);
        }

        String cognome = player.getCognome();
        if (cognome == null || cognome.isEmpty()) {
            return new ResponseEntity<>("Il campo cognome non può essere vuoto", HttpStatus.BAD_REQUEST);
        }

        Query nameQuery = new Query(Criteria.where("nome").is(player.getNome()).and("cognome").is(player.getCognome()));
        Player existingPlayerWithSameNameAndSurname = mongoTemplate.findOne(nameQuery, Player.class);
        if (existingPlayerWithSameNameAndSurname != null) {
            return new ResponseEntity<>("Un giocatore con lo stesso nome e cognome esiste già nel database", HttpStatus.BAD_REQUEST);
        }
        Query nicknameQuery = new Query(Criteria.where("nickname").is(player.getNickname()));
        Player existingPlayerWithSameNickname = mongoTemplate.findOne(nicknameQuery, Player.class);
        if (existingPlayerWithSameNickname != null) {
            return new ResponseEntity<>("Un giocatore con lo stesso nickname esiste già nel database", HttpStatus.BAD_REQUEST);
        }

        Query emailQuery = new Query(Criteria.where("email").is(player.getEmail()));
        Player existingPlayer = mongoTemplate.findOne(emailQuery, Player.class);
        if (existingPlayer != null) {
            return new ResponseEntity<>("L'indirizzo email è già presente nel database", HttpStatus.BAD_REQUEST);
        }

        String email = player.getEmail();
        if (!isValidEmail(email)) {
            return new ResponseEntity<>("L'indirizzo email non è valido", HttpStatus.BAD_REQUEST);
        }

        int age = player.getEta();
        if (age < 14 || age > 99) {
            return new ResponseEntity<>("L'età del giocatore deve essere compresa tra 14 e 99 anni.", HttpStatus.BAD_REQUEST);
        }
        Player newPlayer = mapper.fromPlayerInsertDTOtoPlayerEntity(player);
        newPlayer.setStatusGiocatore(PlayerStatus.ATTIVO);
        newPlayer = mongoTemplate.save(newPlayer);

        return new ResponseEntity<>(newPlayer, HttpStatus.OK);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }


    }

