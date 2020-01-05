package hu.adam.kohoot.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@Builder
public class Game {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany
    private List<Player> players;

    @OneToMany
    private List<GameRound> gameRounds;
}
