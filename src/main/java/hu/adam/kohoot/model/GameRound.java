package hu.adam.kohoot.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String question;
    private String correctAnswer;

    @ElementCollection
    private List<String> possibleAnswers;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long startingTime;
    private Long totalTime;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Game game;

    @Override
    public String toString() {
        return "GameRound{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                ", startingTime=" + startingTime +
                ", totalTime=" + totalTime +
                ", game=" + game.getId() +
                '}';
    }
}
