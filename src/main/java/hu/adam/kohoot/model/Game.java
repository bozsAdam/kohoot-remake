package hu.adam.kohoot.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
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

    @OneToMany(cascade = CascadeType.PERSIST)
    @Singular
    private List<Player> players;

    @OneToMany(cascade = CascadeType.ALL)
    @Singular
    private List<GameRound> gameRounds;

    public void addPlayer(Player player){
        players.add(player);
    }

    public void addGameRound(GameRound gameRound){
        gameRounds.add(gameRound);
    }

    public void removeGameRound(GameRound gameRound) {gameRounds.remove(gameRound); }
}
