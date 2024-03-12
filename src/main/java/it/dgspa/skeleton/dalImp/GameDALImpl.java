package it.dgspa.skeleton.dalImp;
import it.dgspa.skeleton.GameLogic.GameEnum;
import it.dgspa.skeleton.GameLogic.PlayerStatus;
import it.dgspa.skeleton.dal.GameDAL;
import it.dgspa.skeleton.dto.BestPlayerScoreDto;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class GameDALImpl implements GameDAL {


    @Autowired
    MongoTemplate mongoTemplate;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public List<Game> getAllGames() {
        Query query = new Query();
        return mongoTemplate.find(query, Game.class);
    }


    public List<Game> findGamesById(String idplayer) {
        Query query = new Query(Criteria.where("id").is(idplayer));

        return mongoTemplate.find(query, Game.class);
    }


    //crea partita e il sistema in maniera randomica assegna i partecipanti alla partita
    public ResponseEntity<Object> startNewGame(Boolean insert) {
        if (insert) {
            Game g = new Game();
            List<Player> listaPartecipanti = mongoTemplate.findAll( Player.class);

            if (listaPartecipanti.size() < 4) {
                return new ResponseEntity<>("Non ci sono abbastanza partecipanti disponibili per avviare una nuova partita", HttpStatus.BAD_REQUEST);
            }

            Collections.shuffle(listaPartecipanti);

            g.setListaPartecipanti(listaPartecipanti.subList(0, 4)); // Limita la lista a 4 partecipanti
            g.setStatoGioco(GameEnum.INIZIATA);
            g.setClassifica(null);
            g.setDataInizioPartita(new Date());
            g.setDataFinePartita(null);

            try {
                Game game1 = mongoTemplate.save(g);
                return new ResponseEntity<>(game1, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Errore durante il salvataggio della partita", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Inserimento non consentito", HttpStatus.BAD_REQUEST);
        }
    }

    //termina partita con relativa classifica giocatore-punteggio

    public ResponseEntity<Object> closegame(String idGame) {

        Map<String, Integer> classificaRelativa = new HashMap<>();

        Game game1 = mongoTemplate.findById(idGame, Game.class);


        if (game1 == null) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }

        //assegnazione Punteggio

        Integer playerScore = 0;
        for (Player player : game1.getListaPartecipanti()) {

            playerScore = new Random().nextInt(100);

            classificaRelativa.put(player.getNickname(), playerScore);
        }

        // ordinamento classifica decrescente
        Map<String, Integer> classificaOrdinata = classificaRelativa.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Gestione dei pareggi e determinazione del vincitore
        String vincitore = null;
        Integer punteggioMassimo = -1;
        boolean pareggio = false;

        for (Map.Entry<String, Integer> entry : classificaOrdinata.entrySet()) {
            String nickname = entry.getKey();
            Integer score = entry.getValue();
            if (score > punteggioMassimo) {
                vincitore = nickname;
                punteggioMassimo = score;
                pareggio = false;
            } else if (score == punteggioMassimo) {
                pareggio = true;
                vincitore = null;
                break;
            }
        }

        for (Player player : game1.getListaPartecipanti()) {
            int score = classificaRelativa.get(player.getNickname());
            player.setScorePlayer(score);
            if (player.getNickname().equals(vincitore)) {
                player.setWin(true); // Imposta il campo win a true per il vincitore
            } else {
                player.setWin(false); // Imposta il campo win a false per gli altri giocatori
            }
        }
        if (!pareggio) {
            Map<String, Integer> nuovaClassificaRelativa;

            nuovaClassificaRelativa = classificaOrdinata;

            game1.setClassifica(nuovaClassificaRelativa);
            game1.setListaPartecipanti(
                    game1.getListaPartecipanti().stream()
                            .peek(player -> player.setStatusGiocatore(PlayerStatus.NON_ATTIVO))
                            .collect(Collectors.toList())
            );
            game1.setStatoGioco(GameEnum.TERMINATA);
            game1.setDataFinePartita(new Date());
            game1 = mongoTemplate.save(game1);
            return new ResponseEntity<>(game1, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("la partita è finita in pareggio", HttpStatus.OK);
        }
    }





    public ResponseEntity<List<Game>> getAllGamesPlayedByPlayer(String nickname) {

        Query query = new Query(Criteria.where("listaPartecipanti.nickname").is(nickname));
        List<Game> giocatori = mongoTemplate.find(query, Game.class);


        List<Game> partiteTerminate = giocatori.stream()
                .filter(game -> game.getStatoGioco() == GameEnum.TERMINATA)
                .collect(Collectors.toList());

        if (partiteTerminate.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(partiteTerminate, HttpStatus.OK);
    }


    public ResponseEntity<Integer> getCountAllGamesPlayedByPlayer (String nickname){

        Query query = new Query(Criteria.where("listaPartecipanti.nickname").is(nickname));
        List<Game> giocatori = mongoTemplate.find(query, Game.class);

     return  new ResponseEntity<>(giocatori.stream()
             .filter(game -> game.getStatoGioco() == GameEnum.TERMINATA)
             .collect(Collectors.toList()).size(),HttpStatus.OK);

    }


    public ResponseEntity<Integer> countAllGamesPlayerWins(String nickname) {
        Query query = new Query(Criteria.where("listaPartecipanti").elemMatch(Criteria.where("nickname").is(nickname).and("win").is(true)));

        List<Game> winGames = mongoTemplate.find(query, Game.class);

        return new ResponseEntity<>(winGames.stream()
                .filter(g->g.getStatoGioco()==GameEnum.TERMINATA)
                .collect(Collectors.toList()).size(),HttpStatus.OK);
    }




    public ResponseEntity<List<Game>> getAllWinGames(String nickname) {

        Query query = new Query(Criteria.where("listaPartecipanti")
                .elemMatch(Criteria.where("nickname").is(nickname).and("win").is(true)));

        List<Game> winGames = mongoTemplate.find(query, Game.class);

       return new ResponseEntity<>(winGames.stream()
               .filter(g->g.getStatoGioco()==GameEnum.TERMINATA)
               .collect(Collectors.toList()),HttpStatus.OK) ;

    }




    public  ResponseEntity<List<Game>> getAllLostGames(String nickname) {
        Query query = new Query(Criteria.where("listaPartecipanti")
                .elemMatch(Criteria.where("nickname").is(nickname).and("win").is(false)));
        List<Game> lostGames = mongoTemplate.find(query, Game.class);

        return  new ResponseEntity<>(lostGames.stream()
                .filter(g->g.getStatoGioco()==GameEnum.TERMINATA)
                .collect(Collectors.toList()),HttpStatus.OK);
    }

    public ResponseEntity<Integer> countAllGamesPlayerLosses(String nickname){

        Query query = new Query(Criteria.where("listaPartecipanti")
                .elemMatch(Criteria.where("nickname").is(nickname).and("win").is(false)));
        List<Game> lostGames = mongoTemplate.find(query, Game.class);

        return new ResponseEntity<>(lostGames.stream()
                .filter(g->g.getStatoGioco()==GameEnum.TERMINATA)
                .collect(Collectors.toList()).size(),HttpStatus.OK);


    }

    public ResponseEntity<String> getBestAbsoluteScorePlayer (String status){

        Map<String, Integer> punteggioAssoluto = new HashMap<>();
        Query query = new Query(Criteria.where("statoGioco").is(status));
        List<Game> games = mongoTemplate.find(query,Game.class);

        for (Game game : games) {
            Map<String, Integer> classifica = game.getClassifica();
            for (Map.Entry<String, Integer> entry : classifica.entrySet()) {
                String nickname = entry.getKey();
                int score = entry.getValue();
                punteggioAssoluto.put(nickname, Math.max(punteggioAssoluto.getOrDefault(nickname, 0), score));
            }
        }

        int punteggioMassimoAssoluto = punteggioAssoluto.values().stream()
                .max(Integer::compareTo)
                .orElse(0);

        String migliorGiocatore = punteggioAssoluto.entrySet().stream()
                .filter(entry -> entry.getValue() == punteggioMassimoAssoluto)
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);

        return new ResponseEntity<>(migliorGiocatore,HttpStatus.OK);

    }

    public ResponseEntity<Integer> getCountPlusWingGames(String status){

        Map<String, Integer> vittorieGiocatori = new HashMap<>();
        Query query = new Query(Criteria.where("statoGioco").is(status));
        List<Game> games = mongoTemplate.find(query,Game.class);

// Itero su tutti i giochi per contare il numero di vittorie per ogni giocatore
        for (Game game : games) {
            Map<String, Integer> classifica = game.getClassifica();
            for (Player player : game.getListaPartecipanti()) {
                if (player.getWin()) {
                    String nickname = player.getNickname();
                    vittorieGiocatori.put(nickname, vittorieGiocatori.getOrDefault(nickname, 0) + 1);
                }
            }
        }

// Trovo il giocatore che ha vinto più partite
        String migliorGiocatore = Collections.max(vittorieGiocatori.entrySet(), Map.Entry.comparingByValue()).getKey();

// Trovo il numero di partite vinte dal miglior giocatore
        Integer  numeroVittorie = vittorieGiocatori.getOrDefault(migliorGiocatore, 0);

        return new ResponseEntity<>(numeroVittorie,HttpStatus.OK);
    }

    public ResponseEntity<Map<BestPlayerScoreDto, Integer>> getBestPlayerLast5Games() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "dataInizioPartita")).limit(5);
        List<Game> listaUltimeCinquePartite = mongoTemplate.find(query, Game.class);

        Integer punteggioMax = Integer.MIN_VALUE;
        BestPlayerScoreDto giocatore= null;


        for (Game game : listaUltimeCinquePartite) {
            Map<String, Integer> classifica = game.getClassifica();
            if (classifica != null) {
                for (Map.Entry<String, Integer> entry : classifica.entrySet()) {
                    String nickname = entry.getKey();
                    Integer score = entry.getValue();
                    if (score > punteggioMax) {
                        punteggioMax = score;
                        giocatore = new BestPlayerScoreDto(nickname, score);
                    }
                }
            }
        }
        Map<BestPlayerScoreDto, Integer> bestPlayerMap = new HashMap<>();

        bestPlayerMap.put(giocatore, punteggioMax);

        return new ResponseEntity<>(bestPlayerMap,HttpStatus.OK);
    }


}







