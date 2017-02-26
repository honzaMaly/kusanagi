package cz.jan.maly.model.metadata;

import lombok.Getter;

/**
 * Class to identify command manager type
 * Created by Jan on 15-Feb-17.
 */
@Getter
public class CommandManagerKey extends Key {
    public CommandManagerKey(String name) {
        super(name, CommandManagerKey.class);
    }
}
