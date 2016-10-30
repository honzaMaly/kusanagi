package cz.jan.maly.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity describing player of the game
 * Created by Jan on 30-Oct-16.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Builder
@Entity
public class Player implements Serializable {

    @EmbeddedId
    private PlayerId id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "player")
    private List<PlayerPlaysAsRace> playersPlaysAsRaces = new ArrayList<>();

}
