package hu.adam.kohoot.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long id;

    private String name;
    private Integer score;
    private boolean alreadyAnswered;

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
}
