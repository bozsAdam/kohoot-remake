package hu.adam.kohoot.controller;

import hu.adam.kohoot.exception.PlayerNotFoundException;
import hu.adam.kohoot.model.Player;
import hu.adam.kohoot.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/players")
    public List<Player> getAllPlayer(){
        return playerService.getAllPlayer();
    }

    @GetMapping("/players/{id}")
    public Player getPlayer(@PathVariable("id") Long id) throws PlayerNotFoundException {
        return playerService.getPlayer(id);
    }

    @PostMapping("/players")
    public Player savePlayer(@RequestBody Player player){
        return playerService.addPlayer(player);
    }

    @PutMapping("/players/{id}")
    public Player updatePlayer(@PathVariable("id") Long id, @RequestBody Player player) throws PlayerNotFoundException {
        return playerService.updatePlayer(player, id);
    }

    @DeleteMapping("/players/{id}")
    public void removePlayer(@PathVariable("id") Long id) throws PlayerNotFoundException {
        playerService.removePlayer(id);
    }

}
