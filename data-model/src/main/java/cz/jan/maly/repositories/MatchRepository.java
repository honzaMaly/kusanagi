package cz.jan.maly.repositories;

import cz.jan.maly.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for {@link cz.jan.maly.entities.Match}
 * Created by Jan on 30-Oct-16.
 */
public interface MatchRepository extends JpaRepository<Match, Long> {
}
