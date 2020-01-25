package hu.adam.kohoot.model;


import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Integer score;
    private boolean alreadyAnswered;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Game game;

    public void add(Integer score){
        this.score += score;
    }

    public void clearUnwantedFields(){
        id = null;
        score = 0;
        game = null;

    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", alreadyAnswered=" + alreadyAnswered +
                ", gameId=" + game.getId() +
                '}';
    }
}
