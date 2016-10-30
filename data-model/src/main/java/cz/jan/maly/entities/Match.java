package cz.jan.maly.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.*;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity to represent game between two players
 * Created by Jan on 30-Oct-16.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Entity
@Builder
public class Match implements Serializable {
    @EmbeddedId
    private MatchId id;

    @MapsId("firstPlayer")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PlayerPlaysAsRace firstPlayer;

    @MapsId("secondPlayer")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private PlayerPlaysAsRace secondPlayer;

    @MapsId("map")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Map map;
}
