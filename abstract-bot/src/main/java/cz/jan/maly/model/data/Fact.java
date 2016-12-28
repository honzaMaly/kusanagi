package cz.jan.maly.model.data;

import cz.jan.maly.model.agent.Agent;

/**
 * Generic type of knowledge content
 * Created by Jan on 16-Dec-16.
 */
public class Fact<T> {
    private T content;

    protected Fact(T content) {
        if (content instanceof Agent){
            throw new IllegalArgumentException("Content type cannot be type of Agent class!");
        }
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
