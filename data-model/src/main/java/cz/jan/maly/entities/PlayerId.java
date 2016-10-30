package cz.jan.maly.entities;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Composite key for Player entity
 * Created by Jan on 30-Oct-16.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Builder
public class PlayerId implements Serializable {
    private String name;
    private boolean isBot;
}
