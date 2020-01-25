package hu.adam.kohoot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class GameRound {
    @Id
    @GeneratedValue
    private Long id;

    private String question;
    private String correctAnswer;
    private Long startingTime;
    private Long totalTime;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Game game;
}
