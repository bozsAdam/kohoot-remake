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

    @ManyToOne
    private Game game;

    @OneToMany
    private List<Answer> answers;
}
