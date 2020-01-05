package hu.adam.kohoot.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    @Singular
    private List<Player> players;

    @OneToMany
    @Singular
    private List<GameRound> gameRounds;

    public void addPlayer(Player player){
        players.add(player);
    }

    public void addGameRound(GameRound gameRound){
        gameRounds.add(gameRound);
    }
}
