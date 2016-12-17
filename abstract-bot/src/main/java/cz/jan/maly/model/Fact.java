package cz.jan.maly.model;

/**
 * Generic type of knowledge content
 * Created by Jan on 16-Dec-16.
 */
public class Fact<T> {
    private T content;

    protected Fact(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
