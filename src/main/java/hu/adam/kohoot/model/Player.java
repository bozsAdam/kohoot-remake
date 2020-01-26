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
public class Player {

    @Id
    @GeneratedValue
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer score;

    private boolean alreadyAnswered;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Game game;

    public void add(Integer score){
        this.score += score;
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
