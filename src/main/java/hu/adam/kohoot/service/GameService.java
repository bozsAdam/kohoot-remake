package hu.adam.kohoot.service;

import hu.adam.kohoot.beans.Helper;
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
import hu.adam.kohoot.wrapper.QuestionWrapper;
import hu.adam.kohoot.wrapper.ReceivedAnswerWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

    public Game addGame(Game game) {
        log.debug(String.format("Saving game with name %s", game.getRoomName()));
        return gameRepository.save(game);
    }

    public void removeGame(Long id) {
        log.debug(String.format("Removing game with id: %s", id));
        gameRepository.deleteById(id);
    }

    public Game getGame(Long id) throws GameNotFoundException {
        return getGameFromDatabase(id);
    }

    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    public Game updateGame(Game game, Long id) throws GameNotFoundException {
        Game inDbGame = getGame(id);
        BeanUtils.copyProperties(game, inDbGame, Helper.getNullPropertyNames(game));

        return gameRepository.save(inDbGame);
    }

    public void addPlayerToGame(Long playerId, Long gameId) throws GameNotFoundException, PlayerAlreadyInGameException, PlayerNotFoundException {
        Game game = getGameFromDatabase(gameId);
        Player player = getPlayer(playerId);
        if (game.getPlayers().contains(player)) {
            throw new PlayerAlreadyInGameException(String.format("%s is already in game", player.getName()));
        }

        log.debug(String.format("Adding %s to the game", player.getName()));
        game.addPlayer(player);
        log.debug("Saving to the database....");
        player.setGame(game);
        gameRepository.save(game);
    }

    public void removePlayerFromGame(Long playerId, Long gameId) throws GameNotFoundException, PlayerNotFoundException {
        Game game = getGameFromDatabase(gameId);
        Player player = getPlayer(playerId);
        if (game.getPlayers().contains(player)) {
            log.debug(String.format("Removing %s from the game", player.getName()));
            game.removePlayer(player);
            player.setGame(null);
        }

        log.debug("Saving to the database....");
        gameRepository.save(game);
        playerRepository.save(player);
    }

    public Game addGameRounds(List<GameRound> gameRounds, Long gameId) throws GameNotFoundException {
        Game game = getGameFromDatabase(gameId);
        gameRounds.forEach(gameRound -> gameRound.setGame(game));
        game.setGameRounds(gameRounds);

        return gameRepository.save(game);
    }

    public Game addGameRound(GameRound gameRound, Long gameId) throws GameNotFoundException, GameRoundAlreadyInGameException {
        Game game = getGameFromDatabase(gameId);
        if (game.getGameRounds().contains(gameRound)) {
            throw new GameRoundAlreadyInGameException("GameRound is already in Game");
        }

        log.debug("Adding GameRound to the game");
        gameRound.setGame(game);
        game.addGameRound(gameRound);
        log.debug("Saving to the database....");
        return gameRepository.save(game);
    }

    public Integer getRoundsLeft(Long gameId) throws GameNotFoundException {
        Game game = getGameFromDatabase(gameId);
        return game.getGameRounds().size();
    }

    public List<Player> getScoreboard(Long gameId) throws GameNotFoundException {
        Game game = getGameFromDatabase(gameId);
        return game.getPlayers();
    }

    public QuestionWrapper startGameRound(Long gameId) throws GameNotFoundException {
        Game game = getGameFromDatabase(gameId);
        List<GameRound> gameRounds = game.getGameRounds();

        if (gameRounds.isEmpty()) {
            log.debug("GameRound list is empty.");
            return QuestionWrapper.builder()
                    .message("The game has ended")
                    .build();
        }

        GameRound current = gameRounds.get(0);

        if (current.getStartingTime() == null) {
            current.setStartingTime(System.nanoTime());
            gameRounds.set(0, current);
            game.setGameRounds(gameRounds);
            gameRepository.saveAndFlush(game);

            return QuestionWrapper.builder()
                                    .question(current.getQuestion())
                                    .build();
        }

        return QuestionWrapper.builder()
                                .message("There is a game going on right now!!")
                                .build();


    }

    public void endGameRound(Long gameId) throws GameNotFoundException {
        Game game = getGameFromDatabase(gameId);
        List<GameRound> gameRounds = game.getGameRounds();

        if (!gameRounds.isEmpty()) {
            gameRounds.remove(0);
            List<Player> players = game.getPlayers();
            players.forEach(player -> player.setAlreadyAnswered(false));
            game.setPlayers(players);

            gameRepository.saveAndFlush(game);
        }
    }

    public ReceivedAnswerWrapper receiveAnswer(Answer answer) throws GameNotFoundException, PlayerNotFoundException {
        Game game = getGameFromDatabase(answer.getGameId());
        List<GameRound> gameRounds = game.getGameRounds();

        if (!gameRounds.isEmpty()) {
            GameRound current = gameRounds.get(0);
            if (current.getStartingTime() != null) {
                if (current.getCorrectAnswer().equals(answer.getAnswer())) {
                    Player player = getPlayer(answer.getPlayerId());
                    if (!player.isAlreadyAnswered()) {
                        player.add(calculateScore(current.getStartingTime(), current.getTotalTime()));
                        player.setAlreadyAnswered(true);
                        playerRepository.save(player);

                        return ReceivedAnswerWrapper.builder()
                                .received(true)
                                .message("Answer successfully received")
                                .build();
                    }
                }
            } else {
                log.warn("There is no ongoing gameround right now!");
                return ReceivedAnswerWrapper.builder()
                        .received(false)
                        .message("There is no ongoing gameround right now!")
                        .build();
            }
        }

        return ReceivedAnswerWrapper.builder()
                .received(false)
                .message("You already answered in this round")
                .build();
    }

    private Integer calculateScore(Long startingTime, Long totalTime) {
        int result = (int) ((1 - ((float) (System.nanoTime() - startingTime) / totalTime / 2)) * 1000);

        return Math.max(result, 0);
    }

    private Game getGameFromDatabase(Long gameId) throws GameNotFoundException {
        log.debug(String.format("Getting game with id: %s from the database", gameId));
        return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));
    }

    private Player getPlayer(Long id) throws PlayerNotFoundException {
        return playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException("Player not found"));
    }
}
