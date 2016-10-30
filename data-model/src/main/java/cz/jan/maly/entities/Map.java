package cz.jan.maly.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing Map
 * Created by Jan on 30-Oct-16.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"mapName"})
@Getter
@Setter
@Builder
@Entity
public class Map {

    @Id
    private String mapName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "map")
    private List<Match> matches = new ArrayList<>();

}
