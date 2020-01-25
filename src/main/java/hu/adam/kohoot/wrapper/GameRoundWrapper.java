package hu.adam.kohoot.wrapper;

import hu.adam.kohoot.model.GameRound;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameRoundWrapper {
    private List<GameRound> gameRounds;
}
