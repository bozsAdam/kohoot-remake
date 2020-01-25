package hu.adam.kohoot.model;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String roomName;

    @OneToMany(cascade = CascadeType.PERSIST)
    @Singular
    private List<Player> players;

    @OneToMany(cascade = CascadeType.ALL)
    @Singular
    private List<GameRound> gameRounds;

    public void addPlayer(Player player){
        players.add(player);
    }

    public void removePlayer(Player player){ players.remove(player);}

    public void addGameRound(GameRound gameRound){
        gameRounds.add(gameRound);
    }

    public void removeGameRound(GameRound gameRound) {gameRounds.remove(gameRound); }

    public void clearUnwantedFields(){
        id = null;
    }
}
