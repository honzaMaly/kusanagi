package cz.jan.maly.model.knowledge;

import cz.jan.maly.model.metadata.DesireKey;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Container with data to be used to make commitment on (additional to data defined by intention/desire)
 * Created by Jan on 02-Mar-17.
 */
public class DataForDecision {

    //what was already decided on same level - types of desires
    @Getter
    private final List<DesireKey> madeCommitmentToTypes;
    @Getter
    private final List<DesireKey> didNotMakeCommitmentToTypes;

    //desires/intention types to come
    @Getter
    private final List<DesireKey> typesAboutToMakeDecision;

    @Getter
    private final Optional<DesireKey> parentsType;

    @Getter
    private final boolean isAtTopLevel;

    public DataForDecision(Set<DesireKey> desiresToConsider, List<DesireKey> madeCommitmentToTypes, List<DesireKey> didNotMakeCommitmentToTypes, List<DesireKey> typesAboutToMakeDecision, Optional<DesireKey> parentsType) {
        this.isAtTopLevel = !parentsType.isPresent();

        //filter keys
        this.madeCommitmentToTypes = madeCommitmentToTypes.stream()
                .filter(desiresToConsider::contains)
                .collect(Collectors.toList());
        this.didNotMakeCommitmentToTypes = didNotMakeCommitmentToTypes.stream()
                .filter(desiresToConsider::contains)
                .collect(Collectors.toList());
        this.typesAboutToMakeDecision = typesAboutToMakeDecision.stream()
                .filter(desiresToConsider::contains)
                .collect(Collectors.toList());
        this.parentsType = parentsType;
    }

}
