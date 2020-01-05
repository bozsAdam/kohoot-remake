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
public class GameRound {
    @Id
    @GeneratedValue
    private Long id;

    private String question;

    @ElementCollection
    private List<String> answers;

    private String correctAnswer;

    @ManyToOne
    private Game game;
}
