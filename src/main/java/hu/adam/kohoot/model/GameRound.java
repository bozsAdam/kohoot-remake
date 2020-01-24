package hu.adam.kohoot.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRound {
    @Id
    @GeneratedValue
    private Long id;

    private String question;
    private String correctAnswer;
    private Long startingTime;
    private Long totalTime;

    @ManyToOne
    private Game game;
}
