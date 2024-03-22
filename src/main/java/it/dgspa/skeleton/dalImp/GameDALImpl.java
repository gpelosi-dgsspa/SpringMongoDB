package it.dgspa.skeleton.dalImp;
import it.dgspa.skeleton.GameLogic.GameEnum;
import it.dgspa.skeleton.GameLogic.PlayerStatus;
import it.dgspa.skeleton.dal.GameDAL;
import it.dgspa.skeleton.dto.BestPlayerScoreDto;
import it.dgspa.skeleton.dto.GameDto;
import it.dgspa.skeleton.dto.GamePlayerDto;
import it.dgspa.skeleton.dto.PlayerDto;
import it.dgspa.skeleton.entity.Game;
import it.dgspa.skeleton.entity.Player;
import it.dgspa.skeleton.mapper.GamePlayerMapper;
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

    @Autowired
    GamePlayerMapper mapper;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public List<Game> getAllGames() {
        Query query = new Query();
        return mongoTemplate.find(query, Game.class);
    }


    public List<Game> findGamesById(String idGame) {
        Query query = new Query(Criteria.where("idGame").is(idGame));

        return mongoTemplate.find(query, Game.class);
    }

/*

    public ResponseEntity<Object> startNewGame(List<PlayerDto> players) {


        List<String> nicknameGiocatori = players.stream()
                .map(PlayerDto::getNickname)
                .collect(Collectors.toList());

        List<Player> giocatoriASistema = mongoTemplate.find(Query.query(Criteria.where("nickname").in(nicknameGiocatori)), Player.class);
        if (giocatoriASistema.size() != players.size()) {
            return new ResponseEntity<>("Tutti i giocatori passati devono essere registrati nel sistema", HttpStatus.BAD_REQUEST);
        }



        List<Player> giocatori = new ArrayList<>();
        Game g = new Game();
        List<Player> giocatoriPresenti = mongoTemplate.findAll(Player.class);


        // Trasformo ogni PlayerDto in un'entità Player
        for (PlayerDto playerDto : players) {
            // Effettua la mappatura da PlayerDto a Player
            Player player = mapper.fromPlayerDTOtoPlayerEntity(playerDto);
            giocatori.add(player);
        }



        Map<String, Player> giocatoriPresentiMap = new HashMap<>();
        for (Player giocatore : giocatoriPresenti) {
            giocatoriPresentiMap.put(giocatore.getNickname(), giocatore);
        }

// Controllo se i giocatori passati come parametro sono già presenti nel sistema
        for (Player giocatore : giocatori) {
            if (!giocatoriPresentiMap.containsKey(giocatore.getNickname())) {
                return new ResponseEntity<>("Almeno uno dei giocatori non è presente nel sistema", HttpStatus.BAD_REQUEST);
            }
        }

            g.setListaPartecipanti(giocatori);
            g.setListaPartecipanti(
                g.getListaPartecipanti().stream()
                        .peek(player -> player.setStatusGiocatore(PlayerStatus.ATTIVO))
                        .collect(Collectors.toList()));
            g.setStatoGioco(GameEnum.INIZIATA);
            g.setClassifica(null);
            g.setDataInizioPartita(new Date());
            g.setDataFinePartita(null);
            g.setChiusa(false);


            try {
                Game game1 = mongoTemplate.save(g);
                return new ResponseEntity<>(game1, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Errore durante il salvataggio della partita", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
*/


    public ResponseEntity<Object> closegame(GameDto gameDto) {

        Game game1 = new Game();
        List<Player> players = new ArrayList<>();
        Set<String> nicknames = new HashSet<>();

        List<GamePlayerDto> gamePlayerDtos = gameDto.getPlayers();
        for (GamePlayerDto gamePlayerDto : gamePlayerDtos) {
            PlayerDto playerDto = gamePlayerDto.getPlayer();
            Player player = mapper.fromPlayerDTOtoPlayerEntity(playerDto);
            players.add(player);
        }


        game1.setListaPartecipanti(players);


        List<String> nicknameGiocatori = players.stream()
                .map(playerDto -> playerDto.getNickname())
                .collect(Collectors.toList());


        List<Player> giocatoriASistema = mongoTemplate.find(Query.query(Criteria.where("nickname").in(nicknameGiocatori)), Player.class);

        if (giocatoriASistema.size() != players.size()) {
            return new ResponseEntity<>("Tutti i giocatori passati devono essere registrati nel sistema", HttpStatus.BAD_REQUEST);
        }

        for (GamePlayerDto playerDto : gameDto.getPlayers()) {
            String nickname = playerDto.getPlayer().getNickname();
            if (!nicknames.add(nickname)) {
                return new ResponseEntity<>("Non possono esserci due giocatori con lo stesso nickname.", HttpStatus.BAD_REQUEST);
            }

            if (playerDto.getScore() < 0) {
                return new ResponseEntity<>("Lo score di uno dei giocatori non può essere negativo.", HttpStatus.BAD_REQUEST);
            }

            if (playerDto.getPlayer().getEta() < 0) {
                return new ResponseEntity<>("L'età di uno dei giocatori non può essere negativa.", HttpStatus.BAD_REQUEST);
            }
            int eta = playerDto.getPlayer().getEta();
            if (eta <= 15 || eta > 100) {
                return new ResponseEntity<>("L'età di uno dei giocatori deve essere compresa tra 15e 100.", HttpStatus.BAD_REQUEST);
            }
        }





        Map<String, Integer> classificaRelativa = new HashMap<>();
        for (GamePlayerDto playerDto : gameDto.getPlayers()) {
            classificaRelativa.put(playerDto.getPlayer().getNickname(), playerDto.getScore());
        }

        // ordinamento classifica decrescente
        Map<String, Integer> classificaOrdinata = classificaRelativa.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        // determina il vincitore
        List<String> vincitori = new ArrayList<>();
        int punteggioMassimo = classificaOrdinata.entrySet().iterator().next().getValue();
        for (Map.Entry<String, Integer> entry : classificaOrdinata.entrySet()) {
            if (entry.getValue() == punteggioMassimo) {
                vincitori.add(entry.getKey());
            } else {
                break; // Poiché la classifica è ordinata, possiamo interrompere il loop quando troviamo un punteggio inferiore
            }
        }

        // gestione Pareggio
        if (vincitori.size() > 1) {
            game1.getListaPartecipanti().forEach(player -> player.setWin(false));
        } else {
            // Altrimenti, assegno il vincitore
            String vincitore = vincitori.get(0);
            game1.getListaPartecipanti().stream()
                    .filter(player -> player.getNickname().equals(vincitore))
                    .findFirst()
                    .ifPresent(player -> player.setWin(true));
        }

        game1.setDataPartita(gameDto.getDataPartita());
        game1.setClassifica( classificaOrdinata);
        game1.setListaPartecipanti(
                game1.getListaPartecipanti().stream()
                        .peek(player -> player.setStatusGiocatore(PlayerStatus.NON_ATTIVO))
                        .collect(Collectors.toList())
        );
        game1.setStatoGioco(GameEnum.TERMINATA);
        game1.setDataFinePartita(gameDto.getDataPartita());
        game1 = mongoTemplate.save(game1);
        return new ResponseEntity<>(game1, HttpStatus.OK);

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
                    // se ogni giocatore è segnato come vincitore, se si viene incrementato il conteggio
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







