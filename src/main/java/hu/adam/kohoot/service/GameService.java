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

@Service
@Slf4j
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public void addPlayer(Player player, Long gameId) throws GameNotFoundException, PlayerAlreadyInGameException {
        log.debug(String.format("Getting game with id: %s from the database", gameId));
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));
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
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));
        if(game.getGameRounds().contains(gameRound)){
            throw new GameRoundAlreadyInGameException("GameRound is already in Game");
        }

        log.debug("Adding GameRound to the game");
        game.addGameRound(gameRound);
        log.debug("Saving to the database....");
        gameRepository.save(game);
    }
}
