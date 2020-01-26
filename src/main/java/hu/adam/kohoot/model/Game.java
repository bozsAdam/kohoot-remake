package hu.adam.kohoot.model;


import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(unique = true)
    private String roomName;

    @OneToMany(cascade = CascadeType.PERSIST)
    @Singular
    @JsonIgnoreProperties("game")
    @OrderColumn
    private List<Player> players;

    @OneToMany(cascade = CascadeType.ALL)
    @Singular
    @JsonIgnoreProperties("game")
    @OrderColumn
    private List<GameRound> gameRounds;

    public void addPlayer(Player player){
        players.add(player);
    }

    public void removePlayer(Player player){ players.remove(player);}

    public void addGameRound(GameRound gameRound){
        gameRounds.add(gameRound);
    }

    public void removeGameRound(GameRound gameRound) {gameRounds.remove(gameRound); }
}
