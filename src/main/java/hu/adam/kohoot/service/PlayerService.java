package hu.adam.kohoot.service;

import hu.adam.kohoot.exception.PlayerNotFoundException;
import hu.adam.kohoot.model.Player;
import hu.adam.kohoot.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public void addPlayer(Player player){
        log.debug(String.format("Saving player with name %s", player.getName()));
        playerRepository.save(player);
    }

    public void removePlayer(Long id) throws PlayerNotFoundException {
        Player playerToRemove = getPlayerFromDatabase(id);

        log.debug(String.format("Deleting player with name %s", playerToRemove.getName()));
        playerRepository.delete(playerToRemove);
    }

    public Player getPlayer(Long id) throws PlayerNotFoundException {
        return getPlayerFromDatabase(id);
    }

    public void changePlayerName(Long id, String name) throws PlayerNotFoundException {
        Player player = getPlayerFromDatabase(id);
        player.setName(name);

        playerRepository.save(player);
    }

    private Player getPlayerFromDatabase(Long id) throws PlayerNotFoundException {
        log.debug(String.format("Getting player with id %s", id));
        return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(String.format("Player with id %s does not exist", id)));
    }
}
