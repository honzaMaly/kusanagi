package cz.jan.maly.entities;

import cz.jan.maly.enums.Race;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Entity to represent one player in game playing for race
 * Created by Jan on 30-Oct-16.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Builder
@Entity
public class PlayerPlaysAsRace implements Serializable {

    @EmbeddedId
    private PlayerPlaysAsRaceId id;

    @MapsId("player")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Player player;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "firstPlayer")
    private List<Match> matchesAsFirstPlayer;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "secondPlayer")
    private List<Match> matchesAsSecondPlayer;

    @Column(nullable = false)
    private Race race;
}
