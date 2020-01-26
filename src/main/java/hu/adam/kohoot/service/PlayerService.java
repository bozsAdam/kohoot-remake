package hu.adam.kohoot.service;

import hu.adam.kohoot.beans.Helper;
import hu.adam.kohoot.exception.PlayerNotFoundException;
import hu.adam.kohoot.model.Player;
import hu.adam.kohoot.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Player addPlayer(Player player){
        log.debug(String.format("Saving player with name %s", player.getName()));
        return playerRepository.save(player);
    }

    public void removePlayer(Long id) throws PlayerNotFoundException {
        Player playerToRemove = getPlayerFromDatabase(id);

        log.debug(String.format("Deleting player with name %s", playerToRemove.getName()));
        playerRepository.delete(playerToRemove);
    }

    public Player getPlayer(Long id) throws PlayerNotFoundException {
        return getPlayerFromDatabase(id);
    }

    public List<Player> getAllPlayer(){
        return playerRepository.findAll();
    }

    public Player updatePlayer(Player player, Long id) throws PlayerNotFoundException {
        Player inDbPlayer = getPlayer(id);
        player.setScore(inDbPlayer.getScore());
        BeanUtils.copyProperties(player, inDbPlayer, Helper.getNullPropertyNames(player));

        return playerRepository.save(inDbPlayer);
    }

    private Player getPlayerFromDatabase(Long id) throws PlayerNotFoundException {
        log.debug(String.format("Getting player with id %s", id));
        return playerRepository.findById(id).orElseThrow(() -> new PlayerNotFoundException(String.format("Player with id %s does not exist", id)));
    }
}
