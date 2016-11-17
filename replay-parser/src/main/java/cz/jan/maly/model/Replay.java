package cz.jan.maly.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

/**
 * Created by Jan on 17-Nov-16.
 */

@Getter
@AllArgsConstructor
public class Replay {
    private final File file;
    private final boolean isForBots;
}
