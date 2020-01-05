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
    private List<Player> players;

    @OneToMany
    private List<GameRound> gameRounds;
}
