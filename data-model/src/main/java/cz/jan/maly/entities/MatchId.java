package cz.jan.maly.entities;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

/**
 * Composite key for Match
 * Created by Jan on 30-Oct-16.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class MatchId implements Serializable {
    private PlayerPlaysAsRaceId firstPlayer;
    private PlayerPlaysAsRaceId secondPlayer;
    private String map;
    private String fileName;
    private Date date;
}
