package cz.jan.maly.model.agent;

import lombok.Getter;

/**
 * Enumeration of possible actions described by framework in execution order. Only not internal action can be implemented.
 * Internal actions are always executed. Few actions can be executed only once per frame.
 * Created by Jan on 28-Dec-16.
 */
@Getter
public enum ActionCycleEnums {
    MAKE_GAME_OBSERVATION(false, true), READ_MAP(false, true), GET_PART_OF_COMMON_KNOWLEDGE(true, false), REASON_ABOUT_KNOWLEDGE(false, false), SHARE_KNOWLEDGE(true, false),
    READ_REQUESTS_OF_SPECIFIC_TYPES_FROM_OTHER_AGENTS(true, false), READ_OWN_REQUESTS(true, false), MAKE_COMMITMENT_TO_REQUEST(false, false),
    REMOVE_COMMITMENT_TO_REQUEST(false, false), MAKE_REQUEST(false, false), REMOVE_REQUEST_WITH_CONDITION(true, false), REMOVE_REQUEST(false, false),
    ACT_IN_GAME(false, true);

    private final boolean isInternal;
    private final boolean canBeExecutedOncePerFrameOnly;

    ActionCycleEnums(boolean isInternal, boolean canBeExecutedOncePerFrameOnly) {
        this.isInternal = isInternal;
        this.canBeExecutedOncePerFrameOnly = canBeExecutedOncePerFrameOnly;
    }
}
