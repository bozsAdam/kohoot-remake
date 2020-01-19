package hu.adam.kohoot.service;

import hu.adam.kohoot.exception.GameNotFoundException;
import hu.adam.kohoot.exception.GameRoundAlreadyInGameException;
import hu.adam.kohoot.exception.PlayerAlreadyInGameException;
import hu.adam.kohoot.model.Game;
import hu.adam.kohoot.model.GameRound;
import hu.adam.kohoot.model.Player;
import hu.adam.kohoot.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public void addPlayer(Player player, Long gameId) throws GameNotFoundException, PlayerAlreadyInGameException {
        log.debug(String.format("Getting game with id: %s from the database", gameId));
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
        log.debug(String.format("Getting game with id: %s from the database", gameId));
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
            return "The game has ended";
        }

        GameRound current = gameRounds.get(0);

        current.setStartingTime(System.nanoTime());
        gameRounds.set(0,current);
        game.setGameRounds(gameRounds);
        gameRepository.save(game);

        return current.getQuestion();
    }

    private Game getGameFromDatabase(Long gameId) throws GameNotFoundException {
        log.debug(String.format("Getting game with id: %s from the database", gameId));
        return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));
    }
}
