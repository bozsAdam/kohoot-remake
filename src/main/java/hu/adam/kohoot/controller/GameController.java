package hu.adam.kohoot.controller;

import hu.adam.kohoot.exception.GameNotFoundException;
import hu.adam.kohoot.exception.GameRoundAlreadyInGameException;
import hu.adam.kohoot.exception.PlayerAlreadyInGameException;
import hu.adam.kohoot.exception.PlayerNotFoundException;
import hu.adam.kohoot.model.Answer;
import hu.adam.kohoot.model.Game;
import hu.adam.kohoot.model.GameRound;
import hu.adam.kohoot.model.Player;
import hu.adam.kohoot.service.GameService;
import hu.adam.kohoot.wrapper.GameRoundWrapper;
import hu.adam.kohoot.wrapper.QuestionWrapper;
import hu.adam.kohoot.wrapper.ReceivedAnswerWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/games")
    public List<Game> getAllGames() {
        return gameService.getGames();
    }

    @GetMapping("/games/{id}")
    public Game getGame(@PathVariable("id") Long id) throws GameNotFoundException {
        return gameService.getGame(id);
    }

    @PostMapping("/games")
    public Game saveGame(@RequestBody Game game) {
        return gameService.addGame(game);
    }

    @PutMapping("/games/{id}")
    public Game updateGame(@PathVariable("id") Long id, @RequestBody Game game) throws GameNotFoundException {
        return gameService.updateGame(game, id);
    }

    @DeleteMapping("/games/{id}")
    public void removeGame(@PathVariable("id") Long id) {
        gameService.removeGame(id);
    }

    @PutMapping("/games/{gameId}/players/{playerId}")
    public void addPlayerToGame(@PathVariable("gameId") Long gameId, @PathVariable("playerId") Long playerId) throws GameNotFoundException, PlayerAlreadyInGameException, PlayerNotFoundException {
        gameService.addPlayerToGame(playerId, gameId);
    }

    @DeleteMapping("/games/{gameId}/players/{playerId}")
    public void deletePlayerFromGame(@PathVariable("gameId") Long gameId, @PathVariable("playerId") Long playerId) throws GameNotFoundException, PlayerAlreadyInGameException, PlayerNotFoundException {
        gameService.removePlayerFromGame(playerId, gameId);
    }

    @PostMapping("/games/{gameId}/gamerounds")
    public Game addGameRounds(@PathVariable("gameId") Long gameId, @RequestBody GameRoundWrapper gameRounds) throws GameNotFoundException {
        return gameService.addGameRounds(gameRounds.getGameRounds(), gameId);
    }

    @PostMapping("/games/{gameId}/gameround")
    public Game addGameRounds(@PathVariable("gameId") Long gameId, @RequestBody GameRound gameRound) throws GameNotFoundException, GameRoundAlreadyInGameException {
        return gameService.addGameRound(gameRound, gameId);
    }

   @GetMapping("/games/{gameId}/rounds")
   public Integer getRoundsLeft(@PathVariable("gameId") Long gameId) throws GameNotFoundException {
        return gameService.getRoundsLeft(gameId);
   }

   @GetMapping("/games/{gameId}/scoreboard")
    public List<Player> getScoreBoard(@PathVariable("gameId") Long gameId) throws GameNotFoundException {
        return gameService.getScoreboard(gameId);
   }

   @GetMapping("/games/{gameId}/start")
    public QuestionWrapper startGameRound(@PathVariable("gameId") Long gameId) throws GameNotFoundException {
        return new QuestionWrapper(gameService.startGameRound(gameId));
   }

    @GetMapping("/games/{gameId}/end")
    public void endGameRound(@PathVariable("gameId") Long gameId) throws GameNotFoundException {
        gameService.endGameRound(gameId);
    }

    @PostMapping("/games/answers")
    public ReceivedAnswerWrapper receiveAnswer(@RequestBody Answer answer) throws PlayerNotFoundException, GameNotFoundException {
        return gameService.receiveAnswer(answer);
    }

}
