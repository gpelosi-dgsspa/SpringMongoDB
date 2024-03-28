package it.dgspa.skeleton.dalImp;
import it.dgspa.skeleton.GameLogic.GameEnum;
import it.dgspa.skeleton.GameLogic.PlayerStatus;
import it.dgspa.skeleton.dal.GameDAL;
import it.dgspa.skeleton.dto.*;
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

    public ResponseEntity<Object> closegame(GameDto gameDto) {
        Date dataPartita = gameDto.getDataPartita();

        List<Game> partiteTrovate = this.getAllGames();

        Optional<Game> partitaTrovata = partiteTrovate.stream()
                .filter(partita -> partita.getDataPartita().compareTo(dataPartita) == 0)
                .findFirst();

        if (partitaTrovata.isPresent()) {
            return new ResponseEntity<>("Esiste già una partita con la stessa data.", HttpStatus.BAD_REQUEST);
        }
        // Controllo che ci siano esattamente 4 giocatori
        List<GamePlayerDto> players = gameDto.getPlayers();
        if (players.size() != 4) {
            return new ResponseEntity<>("La partita deve essere composta da 4 giocatori.", HttpStatus.BAD_REQUEST);
        }

        int firstScore = players.get(0).getScore();
        boolean allScoresEqual = players.stream().allMatch(player -> player.getScore() == firstScore);
        if (allScoresEqual) {
            return new ResponseEntity<>("Gli score dei giocatori non possono essere tutti uguali.", HttpStatus.BAD_REQUEST);
        }

        // Conversione dei GamePlayerDto in Player
        List<Player> giocatori = players.stream()
                .map(gamePlayerDto -> mapper.fromPlayerDTOtoPlayerEntity(gamePlayerDto.getPlayer()))
                .collect(Collectors.toList());

        Set<String> nicknameSet = new HashSet<>();
        for (Player giocatore : giocatori) {
            if (!nicknameSet.add(giocatore.getNickname())) {
                return new ResponseEntity<>("Non possono esserci due o più giocatori con lo stesso nickname.", HttpStatus.BAD_REQUEST);
            }
        }
        List<String> nicknameGiocatori = giocatori.stream()
                .map(Player::getNickname)
                .collect(Collectors.toList());
        long registeredPlayersCount = mongoTemplate.count(Query.query(Criteria.where("nickname").in(nicknameGiocatori)), Player.class);
        if (registeredPlayersCount != giocatori.size()) {
            return new ResponseEntity<>("Tutti i giocatori devono essere registrati nel sistema.", HttpStatus.BAD_REQUEST);
        }


        // Controllo che lo score di ogni giocatore sia non negativo
        for (GamePlayerDto playerDto : players) {
            if (playerDto.getScore() < 0) {
                return new ResponseEntity<>("Lo score di uno dei giocatori non può essere negativo.", HttpStatus.BAD_REQUEST);
            }
        }

        // Calcolo della classifica
        Map<String, Integer> classifica = players.stream()
                .collect(Collectors.toMap(
                        gamePlayerDto -> gamePlayerDto.getPlayer().getNickname(),
                        GamePlayerDto::getScore
                ));
        classifica = classifica.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Determinazione dei vincitori
        List<String> vincitori = new ArrayList<>();
        int punteggioMassimo = classifica.values().stream().findFirst().orElse(0);
        for (Map.Entry<String, Integer> entry : classifica.entrySet()) {
            if (entry.getValue() == punteggioMassimo) {
                vincitori.add(entry.getKey());
            } else {
                break; // Interrompe il loop quando si incontra un punteggio inferiore, poiché la classifica è ordinata
            }
        }

        // Gestione pareggio
        if (vincitori.size() > 1) {
            giocatori.forEach(player -> player.setWin(false));
        } else {
            String vincitore = vincitori.get(0);
            giocatori.stream()
                    .filter(player -> player.getNickname().equals(vincitore))
                    .findFirst()
                    .ifPresent(player -> player.setWin(true));
        }

        // Creazione della nuova partita
        Game nuovaPartita = new Game();
        nuovaPartita.setListaPartecipanti(giocatori);
        nuovaPartita.setDataPartita(dataPartita);
        nuovaPartita.setClassifica(classifica);
        nuovaPartita.getListaPartecipanti().forEach(player -> player.setStatusGiocatore(PlayerStatus.NON_ATTIVO));
        nuovaPartita.setStatoGioco(GameEnum.TERMINATA);
        nuovaPartita.setDataFinePartita(dataPartita);

        // Salvataggio della nuova partita nel database
        nuovaPartita = mongoTemplate.save(nuovaPartita);

        return new ResponseEntity<>(nuovaPartita, HttpStatus.OK);
    }



    public ResponseEntity<Integer> getCountAllGamesPlayedByPlayer (String nickname){

        Query query = new Query(Criteria.where("listaPartecipanti.nickname").is(nickname));
        List<Game> giocatori = mongoTemplate.find(query, Game.class);

     return  new ResponseEntity<>(giocatori.stream()
             .filter(game -> game.getStatoGioco() == GameEnum.TERMINATA)
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




    public ResponseEntity<List<BestScoreEverDto>> getBestScoreEver() {
        List<Game> games = mongoTemplate.findAll(Game.class);

        Map<String, Integer> partiteGiocate = new HashMap<>();

        Map<String, Integer> scoreTotali = new HashMap<>();


        for (Game game : games) {
            Map<String, Integer> classifica = game.getClassifica();
            if (classifica != null) {
                classifica.forEach((nickname, score) -> {
                    partiteGiocate.merge(nickname, 1, Integer::sum);
                    scoreTotali.merge(nickname, score, Integer::sum);
                });
            }
        }

        // Creazione degli oggetti BestScoreEverDto per ogni giocatore
        List<BestScoreEverDto> giocatoreStatistiche = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : scoreTotali.entrySet()) {
            String nickname = entry.getKey();
            int scoreTotale = entry.getValue();
            int gamesCount = partiteGiocate.getOrDefault(nickname, 0);
            BestScoreEverDto playerStat = new BestScoreEverDto();
            playerStat.setNickname(nickname);
            playerStat.setSumScores(scoreTotale);
            playerStat.setSommaPartiteGiocate(gamesCount);
            giocatoreStatistiche.add(playerStat);
        }


        List<BestScoreEverDto> giocatoreStatisticheOrdinate = giocatoreStatistiche.stream()
                .sorted(Comparator.comparing(BestScoreEverDto::getSumScores).reversed())
                .collect(Collectors.toList());


        List<BestScoreEverDto> giocatoriSelezionati = giocatoreStatisticheOrdinate.stream().limit(3).collect(Collectors.toList());

        return new ResponseEntity<>(giocatoriSelezionati, HttpStatus.OK);
    }


    public ResponseEntity<List<BestPlayerScoreDto>> getBestPlayerLast5Games() {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "dataFinePartita")).limit(5);
        List<Game> ultimeCinquepartite = mongoTemplate.find(query, Game.class);

        List<Map<String, Integer>> allPlayersScores = new ArrayList<>();

        for (Game game : ultimeCinquepartite) {
            Map<String, Integer> classifica = game.getClassifica();
            if (classifica != null) {
                allPlayersScores.add(classifica);
            }
        }

        List<Map.Entry<String, Integer>> allScores = allPlayersScores.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toList());

        List<Map.Entry<String, Integer>> punteggioOrdinato = allScores.stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());

        List<Map.Entry<String, Integer>> top5Scores = punteggioOrdinato.stream().limit(5).collect(Collectors.toList());

        List<BestPlayerScoreDto> result = new ArrayList<>();

        for (int i = 0; i < top5Scores.size(); i++) {
            Map.Entry<String, Integer> entry = top5Scores.get(i);
            BestPlayerScoreDto playerDto = new BestPlayerScoreDto(i + 1, entry.getKey(), entry.getValue());
            result.add(playerDto);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }





}







