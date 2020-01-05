package hu.adam.kohoot.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@RequiredArgsConstructor
@Builder
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Integer score;

    @ManyToOne
    private Game game;
}
