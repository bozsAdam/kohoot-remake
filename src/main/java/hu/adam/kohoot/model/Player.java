package hu.adam.kohoot.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    @ManyToOne
    private Game game;

    @OneToMany
    private List<Answer> answers;
}
