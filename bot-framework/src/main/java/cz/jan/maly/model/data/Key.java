package cz.jan.maly.model.data;

/**
 * Abstract class key to be extended by each class describing some kind of metadata used in framework
 * Created by Jan on 10-Feb-17.
 */
public abstract class Key {
    final String name;
    final int id;

    public Key(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
