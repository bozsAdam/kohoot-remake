package hu.adam.kohoot.service;

import hu.adam.kohoot.exception.GameNotFoundException;
import hu.adam.kohoot.exception.GameRoundAlreadyInGameException;
import hu.adam.kohoot.exception.PlayerAlreadyInGameException;
import hu.adam.kohoot.exception.PlayerNotFoundException;
import hu.adam.kohoot.model.Answer;
import hu.adam.kohoot.model.Game;
import hu.adam.kohoot.model.GameRound;
import hu.adam.kohoot.model.Player;
import hu.adam.kohoot.repository.GameRepository;
import hu.adam.kohoot.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public void addPlayer(Player player, Long gameId) throws GameNotFoundException, PlayerAlreadyInGameException {
        Game game = getGameFromDatabase(gameId);
        if(game.getPlayers().contains(player)){
            throw new PlayerAlreadyInGameException(String.format("%s is already in game", player.getName()));
        }

        log.debug(String.format("Adding %s to the game", player.getName()));
        game.addPlayer(player);
        log.debug("Saving to the database....");
        gameRepository.save(game);
    }

    public void addGameRound(GameRound gameRound, Long gameId) throws GameNotFoundException, GameRoundAlreadyInGameException {
        Game game = getGameFromDatabase(gameId);
        if(game.getGameRounds().contains(gameRound)){
            throw new GameRoundAlreadyInGameException("GameRound is already in Game");
        }

        log.debug("Adding GameRound to the game");
        game.addGameRound(gameRound);
        log.debug("Saving to the database....");
        gameRepository.save(game);
    }

    public Integer getRoundsLeft(Long gameId) throws GameNotFoundException {
        Game game = getGameFromDatabase(gameId);
        return game.getGameRounds().size();
    }

    public List<Player> getScoreboard(Long gameId) throws GameNotFoundException {
        Game game = getGameFromDatabase(gameId);
        return game.getPlayers();
    }

    public String startGameRound(Long gameId) throws GameNotFoundException {
        Game game = getGameFromDatabase(gameId);
        List<GameRound> gameRounds = game.getGameRounds();

        if(gameRounds.isEmpty()){
            log.debug("GameRound list is empty.");
            return "The game has ended";
        }

        GameRound current = gameRounds.get(0);

        current.setStartingTime(System.nanoTime());
        gameRounds.set(0,current);
        game.setGameRounds(gameRounds);
        gameRepository.save(game);

        return current.getQuestion();
    }

    public void endGameRound(Long gameId) throws GameNotFoundException {
        Game game = getGameFromDatabase(gameId);
        List<GameRound> gameRounds = game.getGameRounds();

        if(!gameRounds.isEmpty()){
            gameRounds.remove(0);
            List<Player> players = game.getPlayers();
            players.forEach(player -> player.setAlreadyAnswered(false));
            game.setPlayers(players);
        }
    }

    public void receiveAnswer(Answer answer) throws GameNotFoundException, PlayerNotFoundException {
        Game game = getGameFromDatabase(answer.getGameId());
        List<GameRound> gameRounds = game.getGameRounds();

        if(!gameRounds.isEmpty()){
            GameRound current = gameRounds.get(0);
            if(current.getCorrectAnswer().equals(answer.getAnswer())){
                Player player = playerRepository.findById(answer.getPlayerId())
                                                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));
                if(!player.isAlreadyAnswered()) {
                    player.add(calculateScore(current.getStartingTime(), current.getTotalTime()));
                    player.setAlreadyAnswered(true);
                    playerRepository.save(player);
                }
            }
        }
    }

    private Integer calculateScore(Long startingTime, Long totalTime){
        return (int) ((1 - ((System.nanoTime() - startingTime) / totalTime / 2)) * 1000);
    }

    private Game getGameFromDatabase(Long gameId) throws GameNotFoundException {
        log.debug(String.format("Getting game with id: %s from the database", gameId));
        return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));
    }
}
